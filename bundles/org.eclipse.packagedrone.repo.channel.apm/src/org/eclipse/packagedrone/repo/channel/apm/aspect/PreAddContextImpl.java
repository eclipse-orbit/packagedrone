/*******************************************************************************
 * Copyright (c) 2015, 2016 IBH SYSTEMS GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBH SYSTEMS GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.packagedrone.repo.channel.apm.aspect;

import java.nio.file.Path;
import java.util.Objects;

import org.eclipse.packagedrone.repo.channel.PreAddContext;
import org.eclipse.packagedrone.repo.channel.Veto;
import org.eclipse.packagedrone.repo.channel.search.ArtifactLocator;
import org.eclipse.packagedrone.repo.channel.search.stream.StreamArtifactLocator;

public class PreAddContextImpl implements PreAddContext
{
    private final String name;

    private final Path file;

    private final boolean external;

    private Veto veto;

    private final AspectableContext context;

    public PreAddContextImpl ( final String name, final Path file, final boolean external, final AspectableContext context )
    {
        this.name = name;
        this.file = file;
        this.external = external;
        this.context = context;
    }

    @Override
    public String getName ()
    {
        return this.name;
    }

    @Override
    public Path getFile ()
    {
        return this.file;
    }

    @Override
    public void vetoAdd ( final Veto veto )
    {
        Objects.requireNonNull ( veto );

        if ( this.veto == null )
        {
            // no current veto
            this.veto = veto;
        }
        else if ( this.veto.getPolicy ().ordinal () < veto.getPolicy ().ordinal () )
        {
            // higher ranked veto
            this.veto = veto;
        }
    }

    public Veto getVeto ()
    {
        return this.veto;
    }

    @Override
    public boolean isExternal ()
    {
        return this.external;
    }

    @Override
    public ArtifactLocator getArtifactLocator ()
    {
        return new StreamArtifactLocator ( () -> this.context.getArtifacts ().values ().stream () );
    }
}
