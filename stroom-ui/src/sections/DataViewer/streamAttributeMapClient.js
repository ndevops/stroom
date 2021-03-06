import { actionCreators } from './redux';
import { wrappedGet, wrappedPost } from 'lib/fetchTracker.redux';

export const search = (dataViewerId, pageOffset, pageSize, addResults) => (dispatch, getState) => {
  const state = getState();

  let url = `${state.config.stroomBaseServiceUrl}/streamattributemap/v1/?`;
  url += `pageSize=${pageSize}`;
  url += `&pageOffset=${pageOffset}`;

  wrappedGet(
    dispatch,
    state,
    url,
    (response) => {
      response.json().then((data) => {
        if (addResults) {
          dispatch(actionCreators.add(
            dataViewerId,
            data.streamAttributeMaps,
            data.pageResponse.total,
            pageSize,
            pageOffset,
          ));
        } else {
          dispatch(actionCreators.updateStreamAttributeMaps(
            dataViewerId,
            data.streamAttributeMaps,
            data.pageResponse.total,
            pageSize,
            pageOffset,
          ));
        }
      });
    },
    null,
    true,
  );
};

export const searchWithExpression = (dataViewerId, pageOffset, pageSize, expressionId, addResults) => (
  dispatch,
  getState,
) => {
  const state = getState();
  const expressionState = state.expressionBuilder[expressionId];

  const expression = cleanExpression(expressionState.expression);

  let url = `${state.config.stroomBaseServiceUrl}/streamattributemap/v1/?`;
  url += `pageSize=${pageSize}`;
  url += `&pageOffset=${pageOffset}`;

  wrappedPost(
    dispatch,
    state,
    url,
    (response) => {
      response.json().then((data) => {
        if (addResults) {
          dispatch(actionCreators.add(
            dataViewerId,
            data.streamAttributeMaps,
            data.pageResponse.total,
            pageSize,
            pageOffset,
          ));
        } else {
          dispatch(actionCreators.updateStreamAttributeMaps(
            dataViewerId,
            data.streamAttributeMaps,
            data.pageResponse.total,
            pageSize,
            pageOffset,
          ));
        }
      });
    },
    {
      body: JSON.stringify(expression),
    },
    true,
  );
};

/**
 * TODO: shouldn't actually have to use this -- ideally the ExpressionBuilder would
 * generate JSON compatible with the resource's endpoints. I.e. jackson binding
 * fails if we have these uuids.
 */
const cleanExpression = (expression) => {
  // UUIDs are not part of Expression
  delete expression.uuid;
  expression.children.forEach((child) => {
    delete child.uuid;
  });

  // Occasionally the ExpressionBuilder will put a value on the root, which is wrong.
  // It does this when there's an underscore in the term, e.g. feedName=thing_thing.
  delete expression.value;
  return expression;
};

export const fetchDataSource = dataViewerId => (dispatch, getState) => {
  const state = getState();
  const url = `${state.config.stroomBaseServiceUrl}/streamattributemap/v1/dataSource`;

  wrappedGet(
    dispatch,
    state,
    url,
    (response) => {
      response.json().then((data) => {
        dispatch(actionCreators.updateDataSource(dataViewerId, data));
      });
    },
    null,
    true,
  );
};

export const getDetailsForSelectedRow = dataViewerId => (dispatch, getState) => {
  const state = getState();
  const dataView = state.dataViewers[dataViewerId];
  const streamId = dataView.streamAttributeMaps[dataView.selectedRow].data.id;
  const url = `${state.config.stroomBaseServiceUrl}/streamattributemap/v1/${streamId}`;

  wrappedGet(
    dispatch,
    state,
    url,
    (response) => {
      response.json().then((data) => {
        dispatch(actionCreators.updateDetailsForSelectedRow(dataViewerId, data));
      });
    },
    null,
    true,
  );
};
