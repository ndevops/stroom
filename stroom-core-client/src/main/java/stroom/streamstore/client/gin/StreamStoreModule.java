/*
 * Copyright 2016 Crown Copyright
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
 */

package stroom.streamstore.client.gin;

import stroom.core.client.gin.PluginModule;
import stroom.editor.client.presenter.EditorPresenter;
import stroom.editor.client.presenter.EditorView;
import stroom.editor.client.view.EditorViewImpl;
import stroom.streamstore.client.presenter.ClassificationWrapperPresenter;
import stroom.streamstore.client.presenter.ClassificationWrapperPresenter.ClassificationWrapperView;
import stroom.streamstore.client.presenter.DataPresenter;
import stroom.streamstore.client.presenter.DataPresenter.DataView;
import stroom.streamstore.client.presenter.EntityChoicePresenter;
import stroom.streamstore.client.presenter.EntityChoicePresenter.EntityChoiceView;
import stroom.streamstore.client.presenter.StreamListPresenter;
import stroom.streamstore.client.presenter.StreamPresenter;
import stroom.streamstore.client.presenter.StreamPresenter.StreamView;
import stroom.streamstore.client.presenter.StreamTaskListPresenter;
import stroom.streamstore.client.presenter.StreamTaskPresenter;
import stroom.streamstore.client.presenter.StreamTaskPresenter.StreamTaskView;
import stroom.streamstore.client.presenter.StreamTypeUiManager;
import stroom.streamstore.client.presenter.TextPresenter;
import stroom.streamstore.client.presenter.TextPresenter.TextView;
import stroom.streamstore.client.view.ClassificationWrapperViewImpl;
import stroom.streamstore.client.view.DataViewImpl;
import stroom.streamstore.client.view.EntityChoiceViewImpl;
import stroom.streamstore.client.view.StreamTaskViewImpl;
import stroom.streamstore.client.view.StreamViewImpl;
import stroom.streamstore.client.view.TextViewImpl;
import stroom.widget.dropdowntree.client.presenter.DropDownPresenter.DropDrownView;
import stroom.widget.dropdowntree.client.presenter.DropDownTreePresenter.DropDownTreeView;
import stroom.widget.dropdowntree.client.view.DropDownTreeViewImpl;
import stroom.widget.dropdowntree.client.view.DropDownViewImpl;

public class StreamStoreModule extends PluginModule {
    @Override
    protected void configure() {
        bind(StreamTypeUiManager.class).asEagerSingleton();

        bindPresenterWidget(ClassificationWrapperPresenter.class, ClassificationWrapperView.class,
                ClassificationWrapperViewImpl.class);
        bindPresenterWidget(StreamPresenter.class, StreamView.class, StreamViewImpl.class);
        bindPresenterWidget(EditorPresenter.class, EditorView.class, EditorViewImpl.class);
        bindPresenterWidget(DataPresenter.class, DataView.class, DataViewImpl.class);
        bindPresenterWidget(TextPresenter.class, TextView.class, TextViewImpl.class);
        bindPresenterWidget(StreamTaskPresenter.class, StreamTaskView.class, StreamTaskViewImpl.class);
        bindPresenterWidget(EntityChoicePresenter.class, EntityChoiceView.class, EntityChoiceViewImpl.class);
        bind(StreamListPresenter.class);

        bind(StreamTaskListPresenter.class);

        bindSharedView(DropDrownView.class, DropDownViewImpl.class);
        bindSharedView(DropDownTreeView.class, DropDownTreeViewImpl.class);
    }
}
