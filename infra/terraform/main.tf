terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = ">=3.9.0"
    }
    azurecaf = {
      source  = "aztfmod/azurecaf"
      version = ">=1.2.16"
    }
  }
}

provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "resource_group" {
  name     = var.rg_name
  location = var.location
}

# =================== storage ================
resource "azurecaf_name" "storage_account" {
  name          = var.application_name
  resource_type = "azurerm_storage_account"
  random_length = 5
  clean_input   = true
}

resource "azurerm_storage_account" "application" {
  name                     = azurecaf_name.storage_account.result
  resource_group_name      = azurerm_resource_group.resource_group.name
  location                 = var.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

data "azurerm_client_config" "current" {
}

resource "azurerm_storage_container" "application" {
  name                  = var.container_name
  storage_account_name  = azurerm_storage_account.application.name
  container_access_type = "container"
}

resource "azurerm_role_assignment" "role_storage_blob_data_contributor" {
  scope                = azurerm_storage_container.application.resource_manager_id
  role_definition_name = "Storage Blob Data Contributor"
  principal_id         = data.azurerm_client_config.current.object_id
}