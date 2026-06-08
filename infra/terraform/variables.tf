variable "subscription_id" {
  type        = string
  description = "Azure subscription ID. Leave null to use Azure CLI or env vars."
  default     = null
}

variable "prefix" {
  type        = string
  description = "Prefix used for all resource names."
  default     = "GTILMBC"
}

variable "location" {
  type        = string
  description = "Azure region."
  default     = "swedencentral"
}

variable "resource_group_name" {
  type        = string
  description = "Resource group name. Leave null to derive from prefix."
  default     = "GTILMBC"
}

variable "admin_username" {
  type        = string
  description = "Admin username for the VM."
  default     = "azureuser"
}

variable "admin_ssh_public_key_path" {
  type        = string
  description = "Path to the public SSH key used for VM access."
  default     = "/absolute/path/to/id_rsa.pub"
}

variable "vm_size" {
  type        = string
  description = "Azure VM size."
  default     = "Standard_B2s"
}

variable "ssh_allowed_cidrs" {
  type        = list(string)
  description = "CIDR ranges allowed to access SSH."
  default     = ["0.0.0.0/0"]
}

variable "web_allowed_cidrs" {
  type        = list(string)
  description = "CIDR ranges allowed to access HTTP/HTTPS."
  default     = ["0.0.0.0/0"]
}

variable "tags" {
  type        = map(string)
  description = "Tags to apply to all resources."
  default     = {}
}
