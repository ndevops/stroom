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
import { compose } from 'recompose';
import { connect } from 'react-redux';
import { storiesOf, addDecorator } from '@storybook/react';

import KeyIsDown from './KeyIsDown';
import { KeyIsDownDecorator } from 'lib/storybook/KeyIsDownDecorator';

const TestComponent = ({ keyIsDown }) => (
  <div>
    <h3>Test Keys Down/Up</h3>
    <form>
      <div>
        <label>Control</label>
        <input type='checkbox' checked={keyIsDown.Control} />
      </div>
      <div>
        <label>Cmd/Meta</label>
        <input type='checkbox' checked={keyIsDown.Meta} />
      </div>
      <div>
        <label>Shift</label>
        <input type='checkbox' checked={keyIsDown.Shift} />
      </div>
      <div>
        <label>Alt</label>
        <input type='checkbox' checked={keyIsDown.Alt} />
      </div>
    </form>
  </div>
);

const TestComponentA = compose(connect(({ keyIsDown }) => ({ keyIsDown })), KeyIsDown())(TestComponent);
const TestComponentB = compose(connect(({ keyIsDown }) => ({ keyIsDown })), KeyIsDown(['Control']))(TestComponent);
const TestComponentC = connect(({ keyIsDown }) => ({ keyIsDown }))(TestComponent);

storiesOf('Key Is Down', module)
  .add('Test Component', () => <TestComponentA />)
  .add('Test Component (only detect Control)', () => <TestComponentB />);

storiesOf('Key Is Down (decorated, ctrl, alt)', module)
  .addDecorator(KeyIsDownDecorator(['Control', 'Alt']))
  .add('Test Component', () => <TestComponentC />);
