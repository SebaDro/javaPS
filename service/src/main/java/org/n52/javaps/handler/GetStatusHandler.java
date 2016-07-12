/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
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
package org.n52.javaps.handler;

import static org.n52.javaps.handler.AbstractJobHandler.JOB_ID;

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.request.handler.GenericOperationHandler;
import org.n52.iceland.request.handler.OperationHandlerKey;
import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.wps.JobId;
import org.n52.iceland.ogc.wps.StatusInfo;
import org.n52.iceland.ogc.wps.WPSConstants;
import org.n52.javaps.Engine;
import org.n52.javaps.JobNotFoundException;
import org.n52.javaps.request.GetStatusRequest;
import org.n52.javaps.response.GetStatusResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GetStatusHandler extends AbstractJobHandler
        implements GenericOperationHandler<GetStatusRequest, GetStatusResponse> {
    private static final OperationHandlerKey KEY
            = new OperationHandlerKey(WPSConstants.SERVICE,
                                      WPSConstants.Operations.GetStatus);

    @Inject
    public GetStatusHandler(Engine engine) {
        super(engine, true);
    }

    @Override
    public GetStatusResponse handle(GetStatusRequest request)
            throws OwsExceptionReport {
        String service = request.getService();
        String version = request.getVersion();
        JobId jobId = request.getJobId();
        StatusInfo status;
        try {
            status = getEngine().getStatus(jobId);
        } catch (JobNotFoundException ex) {
            throw new InvalidParameterValueException(JOB_ID, jobId.getValue()).causedBy(ex);
        }
        return new GetStatusResponse(service, version, status);
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.GetStatus.toString();
    }

    @Override
    public Set<OperationHandlerKey> getKeys() {
        return Collections.singleton(KEY);
    }
}
