/*
 * Copyright 2017 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stroom.datasource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import stroom.ExternalService;
import stroom.ServiceDiscoverer;
import stroom.query.api.v1.DocRef;
import stroom.security.SecurityContext;
import stroom.util.spring.StroomScope;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Scope(StroomScope.PROTOTYPE)
public class DataSourceProviderRegistry {
    private final Map<ExternalService, DataSourceProvider> providers = new HashMap<>();
    private final SecurityContext securityContext;
    private final ServiceDiscoverer serviceDiscoverer;

    @Inject
    public DataSourceProviderRegistry(final SecurityContext securityContext, ServiceDiscoverer serviceDiscoverer) {
        this.securityContext = securityContext;
        this.serviceDiscoverer = serviceDiscoverer;

        register(ExternalService.INDEX);
        register(ExternalService.HBASE_STATS);
        register(ExternalService.SQL_STATS);
    }

    public Optional<DataSourceProvider> getDataSourceProvider(final DocRef dataSourceRef) {
        if (dataSourceRef != null) {
            if (dataSourceRef.getType() != null) {
                ExternalService dataSourceService = ExternalService.docRefTypeToServiceMap.get(dataSourceRef);
                return Optional.ofNullable(providers.get(dataSourceService));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    private void register(ExternalService externalService){
        Optional<String> indexAddress = serviceDiscoverer.getAddress(externalService);
        if(indexAddress.isPresent()){
            providers.put(
                externalService,
                new RemoteDataSourceProvider(
                    securityContext,
                    indexAddress.get()
                )
            );
        }
    }
}