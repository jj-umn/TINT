package edu.umn.msi.tropix.storage.service.client;

import edu.umn.msi.tropix.storage.service.StorageService;

public interface StorageServiceFactory {
  
  public StorageService get(final String address);

}
