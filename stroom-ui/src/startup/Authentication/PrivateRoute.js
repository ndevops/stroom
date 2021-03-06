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
import { Route } from 'react-router-dom';
import { compose, withProps } from 'recompose';
import { connect } from 'react-redux';

import { AuthenticationRequest } from 'startup/Authentication';

const enhance = compose(
  connect(
    state => ({
      idToken: state.authentication.idToken,
      // showUnauthorizedDialog: state.login.showUnauthorizedDialog,
      advertisedUrl: state.config.advertisedUrl,
      appClientId: state.config.appClientId,
      authenticationServiceUrl: state.config.authenticationServiceUrl,
      authorisationServiceUrl: state.config.authorisationServiceUrl,
    }),
    {},
  ),
  withProps(({ idToken }) => ({
    isLoggedIn: !!idToken,
  })),
);

const PrivateRoute = ({
  isLoggedIn,
  advertisedUrl,
  appClientId,
  authenticationServiceUrl,
  render,
  ...rest
}) => (
  <Route
    {...rest}
    render={props =>
      (isLoggedIn ? (
        render({...props})
      ) : (
        <AuthenticationRequest
          referrer={props.match.url}
          uiUrl={advertisedUrl}
          appClientId={appClientId}
          authenticationServiceUrl={authenticationServiceUrl}
        />
      ))
    }
  />
);

export default enhance(PrivateRoute);
