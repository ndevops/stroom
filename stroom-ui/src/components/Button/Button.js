/*
 * Copyright 2018 Crown Copyright
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

import React from 'react';
import { PropTypes } from 'prop-types';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

const Button = ({ text, icon, groupPosition, className, circular, selected, ...rest }) => (
  <button
    className={`button ${groupPosition || ''} ${className || ''} ${circular ? 'circular' : ''} ${text ? 'has-text' : ''} ${selected ? 'selected' : ''}`}
    {...rest}>
    {icon ? <FontAwesomeIcon className='button__icon' icon={icon} /> : undefined}
    {text}
  </button>
);

Button.propTypes = {
  icon: PropTypes.string,
  groupPosition: PropTypes.string,
  circular: PropTypes.bool,
  text: PropTypes.string,
  selected: PropTypes.bool,
}

export default Button;