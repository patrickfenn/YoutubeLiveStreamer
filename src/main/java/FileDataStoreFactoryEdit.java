import com.google.api.client.util.IOUtils;
import com.google.api.client.util.Maps;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.AbstractMemoryDataStore;
import com.google.api.client.util.store.DataStore;

import java.io.*;
import java.util.logging.Logger;

/**
 * Edited version, removed raising permission function since it doesn't work on windows.
 * <p>
 * <p>
 * Thread-safe file implementation of a credential store.
 *
 * <p>
 * For security purposes, the file's permissions are set to be accessible only by the file's owner.
 * Note that Java 1.5 does not support manipulating file permissions, and must be done manually or
 * using the JNI.
 * </p>
 *
 * @author Yaniv Inbar
 * @since 1.16
 */
public class FileDataStoreFactoryEdit extends AbstractDataStoreFactory {

    private static final Logger LOGGER = Logger.getLogger(FileDataStoreFactoryEdit.class.getName());

    /**
     * Directory to store data.
     */
    private final File dataDirectory;

    /**
     * @param dataDirectory data directory
     */
    public FileDataStoreFactoryEdit(File dataDirectory) throws IOException {
        dataDirectory = dataDirectory.getCanonicalFile();
        this.dataDirectory = dataDirectory;
        // error if it is a symbolic link
        if (IOUtils.isSymbolicLink(dataDirectory)) {
            throw new IOException("unable to use a symbolic link: " + dataDirectory);
        }
        // create parent directory (if necessary)
        if (!dataDirectory.exists() && !dataDirectory.mkdirs()) {
            throw new IOException("unable to create directory: " + dataDirectory);
        }
        setPermissionsToOwnerOnly(dataDirectory);
    }

    /**
     * Attempts to set the given file's permissions such that it can only be read, written, and
     * executed by the file's owner.
     *
     * @param file the file's permissions to modify
     * @throws IOException
     */
    static void setPermissionsToOwnerOnly(File file) throws IOException {
        // Disable access by other users if O/S allows it and set file permissions to readable and
        // writable by user. Use reflection since JDK 1.5 will not have these methods

    }

    /**
     * Returns the data directory.
     */
    public final File getDataDirectory() {
        return dataDirectory;
    }

    @Override
    protected <V extends Serializable> DataStore<V> createDataStore(String id) throws IOException {
        return new FileDataStore<V>(this, dataDirectory, id);
    }

    /**
     * File data store that inherits from the abstract memory data store because the key-value pairs
     * are stored in a memory cache, and saved in the file (see {@link #save()} when changing values.
     *
     * @param <V> serializable type of the mapped value
     */
    static class FileDataStore<V extends Serializable> extends AbstractMemoryDataStore<V> {

        /**
         * File to store data.
         */
        private final File dataFile;

        FileDataStore(FileDataStoreFactoryEdit dataStore, File dataDirectory, String id)
                throws IOException {
            super(dataStore, id);
            this.dataFile = new File(dataDirectory, id);
            // error if it is a symbolic link
            if (IOUtils.isSymbolicLink(dataFile)) {
                throw new IOException("unable to use a symbolic link: " + dataFile);
            }
            // create new file (if necessary)
            if (dataFile.createNewFile()) {
                keyValueMap = Maps.newHashMap();
                // save the credentials to create a new file
                save();
            } else {
                // load credentials from existing file
                keyValueMap = IOUtils.deserialize(new FileInputStream(dataFile));
            }
        }

        @Override
        public void save() throws IOException {
            IOUtils.serialize(keyValueMap, new FileOutputStream(dataFile));
        }

        @Override
        public FileDataStoreFactoryEdit getDataStoreFactory() {
            return (FileDataStoreFactoryEdit) super.getDataStoreFactory();
        }
    }
}
