package com.appealprocess.appeals.repositories;

import java.util.HashMap;
import java.util.Map.Entry;

import com.appealprocess.appeals.model.Identifier;
import com.appealprocess.appeals.model.Comments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentsRepository {
    
    private static final Logger LOG = LoggerFactory.getLogger(CommentsRepository.class);

    private static final CommentsRepository theRepository = new CommentsRepository();
    private HashMap<String, Comments> backingStore = new HashMap<>(); // Default implementation, not suitable for production!

    public static CommentsRepository current() {
        return theRepository;
    }
    
    private CommentsRepository(){
        
    }
    
   public Comments get(Identifier identifier) {
        return backingStore.get(identifier.toString());
    }
    
    public Comments take(Identifier identifier) {
        Comments comment = backingStore.get(identifier.toString());
        remove(identifier);
        return comment;
    }

    public Identifier store(Comments comment) {
        Identifier id = new Identifier();
        backingStore.put(id.toString(), comment);
        return id;
    }
    
    public void store(Identifier commentIdentifier, Comments comment) {
        backingStore.put(commentIdentifier.toString(), comment);
    }

    public boolean has(Identifier identifier) {
        boolean result =  backingStore.containsKey(identifier.toString());
        return result;
    }

    public void remove(Identifier identifier) {
        backingStore.remove(identifier.toString());
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Entry<String, Comments> entry : backingStore.entrySet()) {
            sb.append(entry.getKey());
            sb.append("\t:\t");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    public synchronized void clear() {
        backingStore = new HashMap<String, Comments>();
    }
}
