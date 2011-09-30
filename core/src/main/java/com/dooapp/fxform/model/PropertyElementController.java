/*
 * Copyright (c) 2011, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform.model;

import javafx.beans.value.WritableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.*;
import java.util.Set;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 07/09/11
 * Time: 14:52
 */
public class PropertyElementController<WrappedType> extends ElementController<WrappedType> implements WritableValue<WrappedType> {

    private final Logger logger = LoggerFactory.getLogger(PropertyElementController.class);

    ValidatorFactory factory;
    Validator validator;

    public PropertyElementController(PropertyElement element) {
        super(element);
        try {
            factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        } catch (ValidationException e) {
            // validation is not activated, since no implementation has been provided
            logger.trace("Validation disabled", e);
        }
    }

    public void setValue(WrappedType o1) {
        // mark controller as dirty
        dirty().set(true);
        if (validator != null) {
            Set<ConstraintViolation<Object>> constraintViolationSet = validator.validateValue((Class<Object>) (element.getSource().getClass()), element.getField().getName(), o1);
            constraintViolations.clear();
            constraintViolations.addAll(constraintViolationSet);
        }
        if (constraintViolations.size() == 0) {
            ((PropertyElement) element).setValue(o1);
        }
    }

}
