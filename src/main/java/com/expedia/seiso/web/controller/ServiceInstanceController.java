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
package com.expedia.seiso.web.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.expedia.seiso.resource.HealthBreakdown;
import com.expedia.seiso.resource.NodeSummary;
import com.expedia.seiso.resource.RotationBreakdown;

/**
 * @author Willie Wheeler
 */
@BasePathAwareController
@RequestMapping("/serviceInstances")
@Slf4j
public class ServiceInstanceController {
	
	@RequestMapping(value = "/{id}/nodeSummary", method = RequestMethod.GET)
	@ResponseBody
	public NodeSummary getNodeSummary(@PathVariable("id") Long id) {
		log.trace("It worked: serviceInstance.id={}", id);
		return new NodeSummary();
	}
	
	@RequestMapping(value = "/{id}/healthBreakdown", method = RequestMethod.GET)
	@ResponseBody
	public HealthBreakdown getHealthBreakdown(@PathVariable("id") Long id) {
		log.trace("It worked: serviceInstance.id={}", id);
		return new HealthBreakdown();
	}
	
	@RequestMapping(value = "/{id}/rotationBreakdown", method = RequestMethod.GET)
	@ResponseBody
	public RotationBreakdown getRotationBreakdown(@PathVariable("id") Long id) {
		log.trace("It worked: serviceInstance.id={}", id);
		return new RotationBreakdown();
	}
}
