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
import { compose, withProps } from 'recompose';
import ReactTooltip from 'react-tooltip';
import uuidv4 from 'uuid/v4';

const enhance = compose(
  // It'd be nice to use the simpler form of tooltip, the one where
  // the content is in the anchor tag. But this only works for simple
  // content and we want to support richer content, e.g. for
  // multi-line tooltips. If we're going to do it this way then we need
  // an ID to tie the anchor tag to the component. We don't have one
  // so to prevent conflicts we'll pass in a UUID.
  withProps(() => ({ uuid: uuidv4() })));

const Tooltip = ({
  trigger, content, className, uuid, ...rest
}) => (
  <React.Fragment>
    <a data-tip data-for={uuid}>
      {trigger}
    </a>
    <ReactTooltip id={uuid} delayShow={1000} className="tooltip-popup raised-low" effect="solid">
      {content}
    </ReactTooltip>
  </React.Fragment>
);

export default enhance(Tooltip);
