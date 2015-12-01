/* 
 * Copyright 2013-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.expedia.seiso.domain.entity.listener;

import javax.persistence.PostPersist;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.expedia.seiso.DataSourceProperties;
import com.expedia.seiso.domain.entity.Service;
import com.expedia.seiso.domain.repo.ServiceRepo;

/**
 * @author Willie Wheeler
 */
@Configurable
@Slf4j
public class ServiceListener {
	@Autowired private ServiceRepo serviceRepo;
	@Autowired private DataSourceProperties dsProps;
	
	@PostPersist
	public void postPersist(@NonNull Service service) {
		log.trace("service={}, serviceRepo={}, dsProps={}", service, serviceRepo, dsProps);
		service.displayDependencies();
	}
}
