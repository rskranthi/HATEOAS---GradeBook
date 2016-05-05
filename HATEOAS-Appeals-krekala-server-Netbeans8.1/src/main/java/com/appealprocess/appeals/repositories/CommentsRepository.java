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
        LOG.debug("CommentsRepository Constructor");
    }
    
    public Comments get(Identifier identifier) {
        LOG.debug("Retrieving Comment object for identifier {}", identifier);
        return backingStore.get(identifier.toString());
    }
    
    public Comments take(Identifier identifier) {
        LOG.debug("Removing the Comments object for identifier {}", identifier);
        Comments comment = backingStore.get(identifier.toString());
        remove(identifier);
        return comment;
    }

    public Identifier store(Comments comment) {
        LOG.debug("Storing a new Comments object");
        
        Identifier id = new Identifier();
        LOG.debug("New comments object's id is {}", id);
        
        backingStore.put(id.toString(), comment);
        return id;
    }
    
    public void store(Identifier identifier, Comments comment) {
        LOG.debug("Storing again the Appeal object with id", identifier);
        backingStore.put(identifier.toString(), comment);
    }

    public boolean has(Identifier identifier) {
        LOG.debug("Checking to see if there is a comment object associated with the id {} in the Comment store", identifier);
        
        boolean result =  backingStore.containsKey(identifier.toString());
        return result;
    }

    public void remove(Identifier identifier) {
        LOG.debug("Removing from storage the Comment object with id", identifier);
        backingStore.remove(identifier.toString());
    }
    
    @Override
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
        backingStore = new HashMap<>();
    }
}
