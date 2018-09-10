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
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { storiesOf, addDecorator } from '@storybook/react';
import 'semantic/dist/semantic.min.css';

import { ReduxDecorator } from 'lib/storybook/ReduxDecorator';
import DataDetails from 'sections/DataViewer/DataDetails';
import { errorData, eventData } from './DataDetails.testData';

storiesOf('DataDetails', module)
  .addDecorator(ReduxDecorator)
  .add('Showing errors', props => <DataDetails data={errorData} />)
  .add('Showing events', props => <DataDetails data={eventData} />);