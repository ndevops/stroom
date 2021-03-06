package stroom.guice;

import com.google.inject.AbstractModule;
import stroom.config.global.impl.db.GlobalConfigDbModule;
import stroom.config.global.impl.db.GlobalConfigModule;
import stroom.data.meta.impl.db.DataMetaDbModule;
import stroom.entity.event.EntityClusterTaskModule;
import stroom.persist.EntityManagerModule;
import stroom.pipeline.scope.PipelineScopeModule;
import stroom.statistics.sql.SQLStatisticsModule;

public class CoreModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new stroom.activity.server.ActivityModule());
        install(new stroom.cache.CacheModule());
        install(new stroom.cache.PipelineCacheModule());
        install(new stroom.dashboard.DashboardModule());
        install(new stroom.dashboard.logging.LoggingModule());
        install(new stroom.datafeed.DataFeedModule());
        install(new stroom.datasource.DatasourceModule());
        install(new stroom.dictionary.DictionaryModule());
        install(new stroom.dictionary.DictionaryHandlerModule());
        install(new stroom.docstore.db.DBPersistenceModule());
        install(new stroom.document.DocumentModule());
        install(new stroom.elastic.impl.ElasticModule());
        install(new stroom.elastic.impl.http.HttpElasticModule());
        install(new stroom.entity.EntityModule());
        install(new stroom.entity.event.EntityEventModule());
        install(new stroom.entity.cluster.EntityClusterModule());
        install(new EntityClusterTaskModule());
        install(new stroom.explorer.ExplorerModule());
        install(new stroom.externaldoc.ExternalDocRefModule());
        install(new stroom.feed.FeedModule());
        install(new PipelineScopeModule());
        install(new stroom.importexport.ImportExportModule());
        install(new stroom.importexport.ImportExportHandlerModule());
        install(new stroom.index.IndexModule());
        install(new stroom.index.IndexElementModule());
        install(new stroom.jobsystem.JobSystemModule());
        install(new stroom.kafka.impl.KafkaModule());
        install(new stroom.kafka.pipeline.KafkaPipelineModule());
        install(new stroom.lifecycle.LifecycleModule());
        install(new stroom.logging.LoggingModule());
        install(new stroom.node.NodeModule());
        install(new stroom.node.NodeHandlerModule());
        install(new stroom.node.NodeServiceModule());
        install(new EntityManagerModule());
        install(new stroom.pipeline.PipelineModule());
        install(new stroom.pipeline.PipelineHandlerModule());
        install(new stroom.pipeline.factory.PipelineFactoryModule());
        install(new stroom.pipeline.factory.CommonPipelineElementModule());
        install(new stroom.pipeline.factory.DataStorePipelineElementModule());
        install(new stroom.pipeline.xsltfunctions.CommonXsltFunctionModule());
        install(new stroom.pipeline.xsltfunctions.DataStoreXsltFunctionModule());
        install(new stroom.pipeline.stepping.PipelineSteppingModule());
        install(new stroom.pipeline.task.PipelineStreamTaskModule());
        install(new stroom.policy.PolicyModule());
//        install(new stroom.properties.impl.PropertyModule());
        install(new GlobalConfigModule());
        install(new GlobalConfigDbModule());
        install(new stroom.query.QueryModule());
        install(new stroom.refdata.ReferenceDataModule());
        install(new stroom.ruleset.RulesetModule());
        install(new stroom.script.ScriptModule());
        install(new stroom.search.SearchModule());
        install(new stroom.search.shard.ShardModule());
        install(new stroom.search.SearchElementModule());
        install(new stroom.security.SecurityModule());
        install(new stroom.servicediscovery.ServiceDiscoveryModule());
        install(new stroom.servlet.ServletModule());
        install(new SQLStatisticsModule());
        install(new stroom.statistics.sql.entity.StatisticStoreModule());
        install(new stroom.statistics.sql.internal.InternalModule());
        install(new stroom.statistics.sql.rollup.SQLStatisticRollupModule());
        install(new stroom.statistics.sql.search.SQLStatisticSearchModule());
        install(new stroom.statistics.stroomstats.entity.StroomStatsStoreModule());
        install(new stroom.statistics.stroomstats.internal.InternalStatisticsModule());
        install(new stroom.statistics.stroomstats.pipeline.StatisticsElementModule());
        install(new stroom.statistics.stroomstats.rollup.StroomStatsRollupModule());
        install(new DataMetaDbModule());
        install(new stroom.data.store.DataStoreHandlerModule());
        install(new stroom.data.store.impl.fs.FileSystemDataStoreModule());
        install(new stroom.streamtask.StreamTaskModule());
        install(new stroom.task.TaskModule());
        install(new stroom.task.cluster.ClusterTaskModule());
        install(new stroom.ui.config.server.UiConfigModule());
        install(new stroom.visualisation.VisualisationModule());
        install(new stroom.volume.VolumeModule());
        install(new stroom.xmlschema.XmlSchemaModule());
    }
}
