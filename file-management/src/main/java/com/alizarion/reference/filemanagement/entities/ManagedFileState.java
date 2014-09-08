package com.alizarion.reference.filemanagement.entities;

/**
 * Describe all managed file states.
 * Created by selim@opnelinux.fr on 08/09/14.
 */
public enum ManagedFileState {


    /**
     * To Remove :  managed file marked to be removed.
     */
    TR,
    /**
     * CoRrupted : the file is corrupted or not on filesystem.
     */
    CR,
    /**
     * DEleted : managed file has been deleted.
     */
    DE,
    /**
     * USed : is the standard state
     */
    US,
    /**
     * Locked : managed file has been locked by the user
     * or the system, and cannot be removed.
     */
    LK,


}
