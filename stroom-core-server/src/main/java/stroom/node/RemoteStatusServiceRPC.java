package stroom.node;

import com.caucho.hessian.server.HessianServlet;

import javax.inject.Inject;
import javax.inject.Named;

//import org.springframework.remoting.caucho.HessianServiceExporter;
//import org.springframework.stereotype.Component;

class RemoteStatusServiceRPC extends HessianServlet implements RemoteStatusService {
    private RemoteStatusService remoteStatusService;

    @Inject
    RemoteStatusServiceRPC(@Named("remoteStatusService") final RemoteStatusService remoteStatusService) {
        this.remoteStatusService = remoteStatusService;

//        setService(remoteStatusService);
//        setServiceInterface(RemoteStatusService.class);
    }

    @Override
    public GetStatusResponse getStatus(final GetStatusRequest request) {
        return remoteStatusService.getStatus(request);
    }
}
