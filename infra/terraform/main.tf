locals {
  resource_group_name = coalesce(var.resource_group_name, "rg-${var.prefix}")
  vnet_name           = "vnet-${var.prefix}"
  subnet_name         = "subnet-${var.prefix}"
  nsg_name            = "nsg-${var.prefix}"
  public_ip_name      = "pip-${var.prefix}"
  nic_name            = "nic-${var.prefix}"
  vm_name             = "vm-${var.prefix}"
  merged_tags         = merge({ project = var.prefix }, var.tags)
}

resource "azurerm_resource_group" "vm" {
  name     = local.resource_group_name
  location = var.location
  tags     = local.merged_tags
}

resource "azurerm_virtual_network" "vm" {
  name                = local.vnet_name
  address_space       = ["10.0.0.0/16"]
  location            = azurerm_resource_group.vm.location
  resource_group_name = azurerm_resource_group.vm.name
  tags                = local.merged_tags
}

resource "azurerm_subnet" "vm" {
  name                 = local.subnet_name
  resource_group_name  = azurerm_resource_group.vm.name
  virtual_network_name = azurerm_virtual_network.vm.name
  address_prefixes     = ["10.0.1.0/24"]
}

resource "azurerm_network_security_group" "vm" {
  name                = local.nsg_name
  location            = azurerm_resource_group.vm.location
  resource_group_name = azurerm_resource_group.vm.name
  tags                = local.merged_tags

  security_rule {
    name                       = "ssh"
    priority                   = 100
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "22"
    source_address_prefixes    = var.ssh_allowed_cidrs
    destination_address_prefix = "*"
  }

  security_rule {
    name                       = "http"
    priority                   = 110
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "80"
    source_address_prefixes    = var.web_allowed_cidrs
    destination_address_prefix = "*"
  }

  security_rule {
    name                       = "https"
    priority                   = 120
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "443"
    source_address_prefixes    = var.web_allowed_cidrs
    destination_address_prefix = "*"
  }
}

resource "azurerm_public_ip" "vm" {
  name                = local.public_ip_name
  location            = azurerm_resource_group.vm.location
  resource_group_name = azurerm_resource_group.vm.name
  allocation_method   = "Static"
  sku                 = "Standard"
  tags                = local.merged_tags
}

resource "azurerm_network_interface" "vm" {
  name                = local.nic_name
  location            = azurerm_resource_group.vm.location
  resource_group_name = azurerm_resource_group.vm.name
  tags                = local.merged_tags

  ip_configuration {
    name                          = "primary"
    subnet_id                     = azurerm_subnet.vm.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.vm.id
  }
}

resource "azurerm_network_interface_security_group_association" "vm" {
  network_interface_id      = azurerm_network_interface.vm.id
  network_security_group_id = azurerm_network_security_group.vm.id
}

resource "azurerm_linux_virtual_machine" "vm" {
  name                = local.vm_name
  resource_group_name = azurerm_resource_group.vm.name
  location            = azurerm_resource_group.vm.location
  size                = var.vm_size
  admin_username      = var.admin_username

  network_interface_ids = [azurerm_network_interface.vm.id]

  disable_password_authentication = true

  admin_ssh_key {
    username   = var.admin_username
    public_key = file(var.admin_ssh_public_key_path)
  }

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-jammy"
    sku       = "22_04-lts"
    version   = "latest"
  }

  tags = local.merged_tags
}
