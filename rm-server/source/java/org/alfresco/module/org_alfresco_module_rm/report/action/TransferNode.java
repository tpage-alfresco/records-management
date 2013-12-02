/*
 * Copyright (C) 2005-2013 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.module.org_alfresco_module_rm.report.action;

import java.io.Serializable;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;

/**
 * Transfer node class
 *
 * @author Tuna Aksoy
 * @since 2.2
 */
public class TransferNode
{
    /** Transfer node reference */
    private NodeRef nodeRef;

    /** Transfer node type */
    private boolean folder;

    /** Transfer node properties */
    private Map<String, Serializable> properties;

    /**
     * @param nodeRef
     * @param folder
     * @param properties
     */
    public TransferNode(NodeRef nodeRef, boolean folder, Map<String, Serializable> properties)
    {
        this.nodeRef = nodeRef;
        this.folder = folder;
        this.properties = properties;
    }

    /**
     * @return transfer node reference
     */
    public NodeRef getNodeRef()
    {
        return this.nodeRef;
    }

    /**
     * @return transfer node type
     */
    public boolean isFolder()
    {
        return this.folder;
    }

    /**
     * @return transfer node properties
     */
    public Map<String, Serializable> getProperties()
    {
        return this.properties;
    }
}
