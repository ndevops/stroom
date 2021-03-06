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

import { applyMiddleware, compose } from 'redux';
import { routerMiddleware } from 'react-router-redux';
import thunk from 'redux-thunk';
import createHistory from 'history/createBrowserHistory';
import persistState from 'redux-localstorage';
import logger from 'redux-logger';

const enhancers = [];
if (process.env.NODE_ENV === 'development') {
  const devToolsExtension = window.devToolsExtension;

  if (typeof devToolsExtension === 'function') {
    enhancers.push(devToolsExtension());
  }
}

export const history = createHistory();

export default compose(
  // TODO: adding storybookMiddleware causes "Error: Accessing nonexistent addons channel, see https://storybook.js.org/basics/faq/#why-is-there-no-addons-channel"
  // applyMiddleware(thunk, routerMiddleware(history), logger, storybookMiddleware),
  applyMiddleware(thunk, routerMiddleware(history), logger),
  persistState('userSettings'),
  ...enhancers,
);
