import java.util.HashMap;
import java.util.Map;

abstract class FileSystemEntry {
    public String name;

    public FileSystemEntry(String name) {
        this.name = name;
    }
 
    abstract boolean isDirectory();
}

class File extends FileSystemEntry {
    public String content;

    public File(String name, String content) {
        super(name);
        this.content = content;
    }

    public boolean isDirectory() {
        return true;
    }
}

class Directory extends FileSystemEntry {
    public Directory parent;
    public Map<String, FileSystemEntry> children;
    

    public Directory(Directory parent, String name) {
        super(name);
        this.parent = parent;
        this.children = new HashMap<>();
    }
    
    public boolean isDirectory() {
        return true;
    }

    public void addChildren(FileSystemEntry fileSystemEntry) {
        children.put(fileSystemEntry.name, fileSystemEntry);
    }
}

class FileSystem { // Singleton
    private final Directory root;

    public FileSystem() {
        this.root = new Directory(null, "/");
    }

    public File createFile(String path, String content) {
        if(path.equals("/")) {
            throw new IllegalArgumentException("Cannot create file at root");
        }

        Directory parent = resolveParent(path);
        String fileName = extractName(path);
        
        if(parent.children.containsKey(fileName) && !parent.children.get(fileName).isDirectory()) {
            throw new IllegalArgumentException("File with same name already exist");
        }

        File file = new File(fileName, content);
        parent.addChildren(file);

        return file;
    }

    public Directory createDirectory(String path) {
        if(path.equals("/")) {
            throw new IllegalArgumentException("Root cannot be created it already exist");
        }

        Directory parent = resolveParent(path);
        String dirName = extractName(path);
        
        if(parent.children.containsKey(dirName) && parent.children.get(dirName).isDirectory()) {
            throw new IllegalArgumentException("Directory with same name already exist");
        }

        Directory directory = new Directory(parent, dirName);
        parent.addChildren(directory);

        return directory;
    }

    public void delete(String path) {
        if(path.equals("/")) {
            throw new IllegalArgumentException("Root cannot be deleted");
        }

        Directory parent = resolveParent(path);
        String name = extractName(path);

        if(!parent.children.containsKey(name)) {
            throw new IllegalArgumentException("File/Directory with name - " + name + " does not exist");
        }

        parent.children.remove(name);
    }

    public void list(String path) {
        FileSystemEntry entry = resolvePath(path);

        if (!entry.isDirectory()) {
            throw new IllegalArgumentException("Cannot list a file");
        }

        System.out.println("Listing path : "+ path);
        for(String names :  ((Directory) entry).children.keySet()) {
            System.out.printf("%s", names);
            System.out.printf("%s", ",");
        }
        System.out.printf("%s", "\n");
    }

    public void move(String srcPath, String destPath) {
        if(srcPath.equals("/")) {
            throw new IllegalArgumentException("Root cannot be moved");
        }

        Directory srcParent = resolveParent(srcPath);
        String srcName = extractName(srcPath);

        if(!srcParent.children.containsKey(srcName)) {
            throw new IllegalArgumentException(srcName + " does not exist");
        }

        FileSystemEntry fileSystemEntry = srcParent.children.get(srcName);
        

        Directory destParent = resolveParent(destPath);
        String destName = extractName(destPath);

        if(destParent.children.containsKey(destName)) {
            throw new IllegalStateException("Destination already exists: " + destPath);
        }

        if(fileSystemEntry.isDirectory()) {
            Directory current = destParent;
            while(current != null){
                if(current == fileSystemEntry) {
                    throw new IllegalArgumentException("Cannot move folder into itself");
                }

                current = current.parent;
            }
        }


        srcParent.children.remove(srcName);
        fileSystemEntry.name= destName;
        destParent.children.put(destName, fileSystemEntry);
    }

    private Directory resolveParent(String path) {
        if(path.equals("/")) {
            throw new IllegalArgumentException("Root has no parent");
        }

        int lastSlash = path.lastIndexOf("/");
        String parentPath = lastSlash == 0 ? "/" : path.substring(0, lastSlash);

        Directory parent = (Directory)resolvePath(parentPath);

        if(!parent.isDirectory()) {
            throw new IllegalArgumentException("Parent: "+ parent.name + " is not a directory." );
        }

        return parent;
    }

    private FileSystemEntry resolvePath(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be empty");
        }

        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Path must be absolute");
        }

        if (path.equals("/")) {
            return root;
        }

        String[] parts = path.substring(1).split("/");
        FileSystemEntry current = root;

        for (String part : parts) {
            if (part.isEmpty()) {
                throw new IllegalArgumentException("Invalid path: consecutive slashes");
            }

            if (!current.isDirectory()) {
                throw new IllegalArgumentException("Not a directory");
            }
            Directory childDirectory = ((Directory) current);
            FileSystemEntry child = childDirectory.children.get(part);
            if (child == null) {
                throw new IllegalArgumentException("Path not found: " + path);
            }

            current = child;
        }

        return current;
    }

    private String extractName(String path) {
        int lastSlash = path.lastIndexOf("/");
        return path.substring(lastSlash+1);
    }
}

class InMemoryFileSystem {
    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        fs.createDirectory("/home");
        fs.createDirectory("/usr");
        fs.createDirectory("/var");
        fs.list("/");

        fs.createDirectory("/home/nktiwari");
        fs.createDirectory("/home/nktiwari/Desktop");
        fs.createFile("/home/nktiwari/Desktop/LLD.java", "in-memory-fs");
        fs.list("/home/nktiwari/Desktop");

        fs.delete("/usr");
        fs.list("/");
    }
}
