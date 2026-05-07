"""Extract Markdown lecture contents from an Artemis XHTML export.

The script reads the exported XHTML file, finds every lecture unit with a
<content> element, and writes each markdown block to its own file.

By default, file names are derived from the lecture unit name and the first
markdown heading inside the content, prefixed with the unit order so the
output stays stable across runs.
"""

from __future__ import annotations

import argparse
import re
import unicodedata
from pathlib import Path
from typing import Iterable
from xml.etree import ElementTree as ET


def slugify(value: str) -> str:
	"""Convert a string into a filesystem-friendly ASCII slug."""

	normalized = unicodedata.normalize("NFKD", value)
	ascii_value = normalized.encode("ascii", "ignore").decode("ascii")
	slug = re.sub(r"[^a-zA-Z0-9]+", "-", ascii_value).strip("-").lower()
	return slug or "untitled"


def first_markdown_heading(content: str) -> str:
	"""Return the first markdown heading text found in a content block."""

	for line in content.splitlines():
		stripped = line.strip()
		if not stripped:
			continue
		match = re.match(r"^#{1,6}\s+(.+)$", stripped)
		if match:
			return match.group(1).strip()
		break
	return ""


def text_or_empty(element: ET.Element | None) -> str:
	"""Return element text or an empty string when the element is missing."""

	return "" if element is None or element.text is None else element.text


def normalize_content(content: str) -> str:
	"""Remove export artefacts that are not part of the markdown content."""

	lines = content.splitlines()
	if lines and lines[0].strip() == "##":
		lines = lines[1:]
	return "\n".join(lines).strip()


def iter_lecture_units(root: ET.Element) -> Iterable[tuple[int, str, str]]:
	"""Yield lecture unit order, name, and markdown content."""

	lecture_units = root.findall(".//lectureUnits/lectureUnits")
	for index, lecture_unit in enumerate(lecture_units, start=1):
		name = text_or_empty(lecture_unit.find("name")).strip()
		content = normalize_content(text_or_empty(lecture_unit.find("content")))
		if not content:
			continue
		yield index, name or f"lecture-unit-{index}", content


def build_output_name(order: int, unit_name: str, content: str) -> str:
	"""Build a meaningful, stable markdown file name for one lecture unit."""

	heading = first_markdown_heading(content)
	parts = [f"{order:02d}", slugify(unit_name)]
	if heading:
		parts.append(slugify(heading))
	return "-".join(part for part in parts if part) + ".md"


def extract_markdown_files(input_path: Path, output_dir: Path) -> list[Path]:
	"""Extract lecture unit markdown blocks into separate files."""

	tree = ET.parse(input_path)
	root = tree.getroot()

	output_dir.mkdir(parents=True, exist_ok=True)
	written_files: list[Path] = []

	for order, unit_name, content in iter_lecture_units(root):
		output_name = build_output_name(order, unit_name, content)
		output_path = output_dir / output_name
		output_path.write_text(content.rstrip() + "\n", encoding="utf-8")
		written_files.append(output_path)

	return written_files


def parse_args() -> argparse.Namespace:
	"""Parse CLI arguments for the extractor."""

	parser = argparse.ArgumentParser(
		description="Extract markdown lecture contents from an Artemis XHTML export."
	)
	parser.add_argument(
		"input_path",
		nargs="?",
		default="input.xhtml",
		help="Path to the Artemis XHTML export file.",
	)
	parser.add_argument(
		"output_dir",
		nargs="?",
		default="markdown",
		help="Directory where the markdown files will be written.",
	)
	return parser.parse_args()


def main() -> int:
	"""Run the extractor from the command line."""

	args = parse_args()
	input_path = Path(args.input_path)
	output_dir = Path(args.output_dir)

	written_files = extract_markdown_files(input_path, output_dir)
	for path in written_files:
		print(path)

	return 0


if __name__ == "__main__":
	raise SystemExit(main())
