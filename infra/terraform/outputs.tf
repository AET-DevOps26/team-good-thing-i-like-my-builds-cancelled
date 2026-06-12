# output "resource_group_name" {
#   value = azurerm_resource_group.vm.name
# }

output "public_ip_address" {
  value = azurerm_public_ip.pip.ip_address
}

output "ssh_command" {
  value = "ssh ${var.admin_username}@${azurerm_public_ip.pip.ip_address}"
}

output "vm_id" {
  value = azurerm_linux_virtual_machine.vm.id
}
