terraform {
  required_version = ">= 1.6.0"

  backend "azurerm" {
      resource_group_name = "GTILMBC"
      storage_account_name = "gtilmbctfstate"
      container_name       = "tfstate"
      key                  = "terraform.tfstate"
      use_azuread_auth = true
  }

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.100"
    }
  }
}

provider "azurerm" {
  features {}
  subscription_id = var.subscription_id
}
