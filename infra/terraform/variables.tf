variable "rg_name" {
  type        = string
  description = "The resource group name of your application."
  default     = "blob-rs"
}


variable "application_name" {
  type        = string
  description = "The name of your application."
  default     = "blob-data"
}

variable "location" {
  type        = string
  description = "The Azure region where all resources in this example should be created."
  default     = "australiaeast"
}

variable "container_name" {
  type        = string
  description = "The container name of the blob storage"
  default     = "blobpocdatacontainer"
}