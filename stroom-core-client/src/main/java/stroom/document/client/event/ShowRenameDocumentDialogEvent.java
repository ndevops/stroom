/*
 * Copyright 2017 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package stroom.document.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import stroom.explorer.shared.ExplorerNode;

import java.util.List;

public class ShowRenameDocumentDialogEvent extends GwtEvent<ShowRenameDocumentDialogEvent.Handler> {
    private static Type<Handler> TYPE;
    private final List<ExplorerNode> explorerNodeList;

    private ShowRenameDocumentDialogEvent(final List<ExplorerNode> explorerNodeList) {
        this.explorerNodeList = explorerNodeList;
    }

    public static void fire(final HasHandlers handlers, final List<ExplorerNode> explorerNodeList) {
        handlers.fireEvent(new ShowRenameDocumentDialogEvent(explorerNodeList));
    }

    public static Type<Handler> getType() {
        if (TYPE == null) {
            TYPE = new Type<>();
        }
        return TYPE;
    }

    @Override
    public final Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final Handler handler) {
        handler.onRename(this);
    }

    public List<ExplorerNode> getExplorerNodeList() {
        return explorerNodeList;
    }

    public interface Handler extends EventHandler {
        void onRename(final ShowRenameDocumentDialogEvent event);
    }
}
