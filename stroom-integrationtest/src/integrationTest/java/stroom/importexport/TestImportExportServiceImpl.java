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

package stroom.importexport;

import org.junit.Assert;
import org.junit.Test;
import stroom.docref.DocRef;
import stroom.entity.shared.DocRefs;
import stroom.explorer.api.ExplorerNodeService;
import stroom.explorer.api.ExplorerService;
import stroom.explorer.shared.ExplorerConstants;
import stroom.explorer.shared.ExplorerNode;
import stroom.feed.FeedStore;
import stroom.feed.shared.FeedDoc;
import stroom.importexport.shared.ImportState;
import stroom.pipeline.PipelineStore;
import stroom.pipeline.shared.PipelineDoc;
import stroom.resource.ResourceStore;
import stroom.test.AbstractCoreIntegrationTest;
import stroom.util.shared.ResourceKey;
import stroom.util.test.FileSystemTestUtil;

import javax.inject.Inject;
import java.util.List;

public class TestImportExportServiceImpl extends AbstractCoreIntegrationTest {
    @Inject
    private ImportExportService importExportService;
    @Inject
    private ResourceStore resourceStore;
    @Inject
    private PipelineStore pipelineStore;
    @Inject
    private FeedStore feedStore;
    @Inject
    private ExplorerService explorerService;
    @Inject
    private ExplorerNodeService explorerNodeService;

    @Test
    public void testExport() {
        // commonTestControl.deleteDir();

        final DocRef system = explorerNodeService.getRoot().map(ExplorerNode::getDocRef).orElse(null);
        Assert.assertEquals(1, explorerNodeService.getDescendants(system).size());

        final DocRef folder1 = explorerService.create(ExplorerConstants.FOLDER, "Root1_", system, null);
        DocRef folder2 = explorerService.create(ExplorerConstants.FOLDER, "Root2_" + FileSystemTestUtil.getUniqueTestString(), system, null);
        DocRef folder2child1 = explorerService.create(ExplorerConstants.FOLDER, "Root2_Child1_" + FileSystemTestUtil.getUniqueTestString(), folder2, null);
        DocRef folder2child2 = explorerService.create(ExplorerConstants.FOLDER, "Root2_Child2_" + FileSystemTestUtil.getUniqueTestString(), folder2, null);

        Assert.assertEquals(5, explorerNodeService.getDescendants(system).size());

        final DocRef tran1Ref = explorerService.create(PipelineDoc.DOCUMENT_TYPE, FileSystemTestUtil.getUniqueTestString(), folder1, null);
        final PipelineDoc tran1 = pipelineStore.readDocument(tran1Ref);
        tran1.setDescription("Description");
        pipelineStore.writeDocument(tran1);

        final DocRef tran2Ref = explorerService.create(PipelineDoc.DOCUMENT_TYPE, FileSystemTestUtil.getUniqueTestString(), folder2, null);
        PipelineDoc tran2 = pipelineStore.readDocument(tran2Ref);
        tran2.setDescription("Description");
        tran2.setParentPipeline(tran1Ref);
        tran2 = pipelineStore.writeDocument(tran2);

        final DocRef referenceFeedRef = explorerService.create(FeedDoc.DOCUMENT_TYPE, FileSystemTestUtil.getUniqueTestString(), folder1, null);
        final FeedDoc referenceFeed = feedStore.readDocument(referenceFeedRef);
        referenceFeed.setDescription("Description");
        feedStore.writeDocument(referenceFeed);

        final DocRef eventFeedRef = explorerService.create(FeedDoc.DOCUMENT_TYPE, FileSystemTestUtil.getUniqueTestString(), folder2, null);
        FeedDoc eventFeed = feedStore.readDocument(eventFeedRef);
        eventFeed.setDescription("Description");
        // eventFeed.getReferenceFeed().add(referenceFeed);
        eventFeed = feedStore.writeDocument(eventFeed);

        final DocRef eventFeedChildRef = explorerService.create(FeedDoc.DOCUMENT_TYPE, FileSystemTestUtil.getUniqueTestString(), folder2child1, null);
        final FeedDoc eventFeedChild = feedStore.readDocument(eventFeedChildRef);
        eventFeedChild.setDescription("Description");
        // eventFeedChild.getReferenceFeed().add(referenceFeed);
        feedStore.writeDocument(eventFeedChild);

        final int startTranslationSize = pipelineStore.list().size();
        final int startFeedSize = feedStore.list().size();

        final ResourceKey file = resourceStore.createTempFile("Export.zip");
        final DocRefs docRefs = new DocRefs();
        docRefs.add(folder1);
        docRefs.add(folder2);

        // Export
        importExportService.exportConfig(docRefs, resourceStore.getTempFile(file), null);

        final ResourceKey exportConfig = resourceStore.createTempFile("ExportPlain.zip");

        importExportService.exportConfig(docRefs, resourceStore.getTempFile(exportConfig), null);

        // Delete it and check
        pipelineStore.deleteDocument(tran2.getUuid());
        Assert.assertEquals(startTranslationSize - 1, pipelineStore.list().size());

        feedStore.deleteDocument(eventFeedRef.getUuid());
        Assert.assertEquals(startFeedSize - 1, feedStore.list().size());

        // Import
        final List<ImportState> confirmations = importExportService
                .createImportConfirmationList(resourceStore.getTempFile(file));

        for (final ImportState confirmation : confirmations) {
            confirmation.setAction(true);
        }

        importExportService.performImportWithConfirmation(resourceStore.getTempFile(file), confirmations);

        Assert.assertEquals(startFeedSize, feedStore.list().size());
        Assert.assertEquals(startTranslationSize, pipelineStore.list().size());

        final ResourceKey fileChild = resourceStore.createTempFile("ExportChild.zip");
        final DocRefs criteriaChild = new DocRefs();
        criteriaChild.add(folder2child2);

        // Export
        importExportService.exportConfig(criteriaChild, resourceStore.getTempFile(fileChild), null);
    }
}
