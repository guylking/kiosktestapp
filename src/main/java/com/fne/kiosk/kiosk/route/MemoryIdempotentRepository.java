package com.fne.kiosk.kiosk.route;

import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.api.management.ManagedOperation;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.spi.IdempotentRepository;
import org.apache.camel.support.service.ServiceSupport;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@ManagedResource(description="Memory Based repository store")
public class MemoryIdempotentRepository extends ServiceSupport implements IdempotentRepository  {

    private Map<String, Object> cache;
    private int cacheSize;

    public MemoryIdempotentRepository() {
        this.cache = new HashMap<String,Object>(1000);
    }

    public MemoryIdempotentRepository(Map<String,Object> cache) {
        this.cache = cache;
    }

    public static IdempotentRepository memoryIdempotentRepository() {
        return( new MemoryIdempotentRepository());
    }

    public static IdempotentRepository memoryIdempotentRepository( int cacheSize ) {
        return( new MemoryIdempotentRepository());
    }

    @ManagedOperation( description="Add the key to the store")
    public boolean add( String key ) {
        synchronized (cache) {
            File f = new File(key);
            long modified=f.lastModified();
            String newKey = key+modified;
            if( cache.containsKey(newKey)) {
                return false;
            } else {
                cache.put( newKey, newKey);
                return true;
            }
        }
    }

    @ManagedOperation(description = "Dees the store contain the given Key")
    public boolean contains( String key ) {

        synchronized (cache) {
            File f = new File(key);
            long modified=f.lastModified();
            String newKey = key+modified;
            return( cache.containsKey( newKey ) );
        }
    }

    @ManagedOperation(description = "Remove the Key from storage")
    public boolean remove( String key ) {

        synchronized (cache) {
            File f = new File(key);
            long modified=f.lastModified();
            String newKey = key+modified;
            return( cache.remove( newKey ) != null );
        }
    }

    public boolean confirm( String key ) {
        return true;
    }

    public Map<String, Object> getCache() {
        return cache;
    }

    @ManagedAttribute(description = "Return the cache size")
    public int getCacheSize( ) {
        return cache.size();
    }

    public void setCacheSize( int cacheSize ) {
        this.cacheSize = cacheSize;
    }

    @Override
    protected void doStart() throws  Exception {
        if( cacheSize > 0 ) {
            cache = new HashMap<>(cacheSize);
        }
    }

    @Override
    protected void doStop() throws  Exception {
        cache.clear();
    }

    @Override
    public void clear() {

    }
}
