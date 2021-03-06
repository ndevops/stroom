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
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { compose, lifecycle, branch, renderComponent, withHandlers } from 'recompose';
import PanelGroup from 'react-panelgroup';
import HorizontalPanel from 'components/HorizontalPanel';
import Mousetrap from 'mousetrap';
import 'react-table/react-table.css';

import Loader from 'components/Loader';
import IconHeader from 'components/IconHeader';
import ExpressionSearchBar from 'components/ExpressionSearchBar';
import {
  search,
  getDetailsForSelectedRow,
  fetchDataSource,
  searchWithExpression,
} from './streamAttributeMapClient';
import { getDataForSelectedRow } from './dataResourceClient';
import DetailsTabs from './DetailsTabs';
import withLocalStorage from 'lib/withLocalStorage';
import DataList from './DataList/DataList';
import { actionCreators } from './redux';

const withListHeight = withLocalStorage('listHeight', 'setListHeight', 500);
const withDetailsHeight = withLocalStorage('detailsHeight', 'setDetailsHeight', 500);

const { selectRow, deselectRow } = actionCreators;

const enhance = compose(
  withListHeight,
  withDetailsHeight,
  connect(
    (state, props) => {
      let dataSource, streamAttributeMaps, pageSize, pageOffset, selectedRow, dataForSelectedRow, detailsForSelectedRow;

      if (state.dataViewers[props.dataViewerId] !== undefined) {
        // Parentheses required when destructuring separatly from declaration.
        ({ dataSource, streamAttributeMaps, pageSize, pageOffset, selectedRow, dataForSelectedRow, detailsForSelectedRow } = state.dataViewers[props.dataViewerId]);
      }

      return {
        streamAttributeMaps: streamAttributeMaps || [],
        pageSize: pageSize || 0,
        pageOffset: pageOffset || 20,
        selectedRow,
        dataForSelectedRow,
        detailsForSelectedRow,
        dataSource,
      };
    },
    {
      search,
      searchWithExpression,
      fetchDataSource,
      selectRow,
      deselectRow,
      getDataForSelectedRow,
      getDetailsForSelectedRow,
    },
  ),
  withHandlers({
    onRowSelected: ({ selectRow, getDataForSelectedRow, getDetailsForSelectedRow }) => (
      dataViewerId,
      selectedRow,
    ) => {
      selectRow(dataViewerId, selectedRow);
      getDataForSelectedRow(dataViewerId);
      getDetailsForSelectedRow(dataViewerId);
    },
    onHandleLoadMoreRows: ({
      searchWithExpression, dataViewerId, pageOffset, pageSize,
    }) => () => {
      searchWithExpression(dataViewerId, pageOffset, pageSize, dataViewerId);
      // TODO: need to search with expression too
      // search(dataViewerId, pageOffset + 1, pageSize, true);
    },
    onMoveSelection: ({
      selectRow,
      streamAttributeMaps,
      selectedRow,
      getDataForSelectedRow,
      getDetailsForSelectedRow,
      search,
      dataViewerId,
      pageOffset,
      pageSize,
      searchWithExpression,
    }) => (direction) => {
      const isAtEndOfList = selectedRow === streamAttributeMaps.length - 1;
      if (isAtEndOfList) {
        searchWithExpression(dataViewerId, pageOffset, pageSize, dataViewerId, true);
        // search(dataViewerId, pageOffset + 1, pageSize, dataViewerId, true);
      } else {
        if (direction === 'down') {
          selectedRow += 1;
        } else if (direction === 'up') {
          selectedRow -= 1;
        }

        // TODO: stop repeating onRowSelected here
        selectRow(dataViewerId, selectedRow);
        getDataForSelectedRow(dataViewerId);
        getDetailsForSelectedRow(dataViewerId);
      }
    },
  }),
  lifecycle({
    componentDidMount() {
      const {
        search,
        dataViewerId,
        // pageSize,
        // pageOffset,
        selectedRow,
        fetchDataSource,
        onMoveSelection,
        // searchWithExpression,
        // processSearchString,
      } = this.props;

      fetchDataSource(dataViewerId);

      // // We need to set up an expression so we've got something to search with,
      // // even though it'll be empty.
      // const { expressionChanged, expressionId, dataSource } = this.props;
      // const parsedExpression = processSearchString(dataSource, '');
      // expressionChanged(expressionId, parsedExpression.expression);

      // // If we're got a selectedRow that means the user has already been to this page.
      // // Re-doing the search will wipe out their previous location, and we want to remember it.
      if (!selectedRow) {
        // searchWithExpression(dataViewerId, pageOffset, pageSize, dataViewerId);
        search(dataViewerId, 0, 400);
      }

      Mousetrap.bind('up', () => onMoveSelection('up'));
      Mousetrap.bind('down', () => onMoveSelection('down'));
    },
    componentWillUnmount() {
      Mousetrap.unbind('up');
      Mousetrap.unbind('down');
    },
  }),
  branch(
    ({ dataSource }) => !dataSource,
    renderComponent(() => <Loader message="Loading data source" />),
  ),
);

const DataViewer = ({
  dataViewerId,
  pageOffset,
  pageSize,
  deselectRow,
  selectedRow,
  dataForSelectedRow,
  detailsForSelectedRow,
  listHeight,
  setListHeight,
  detailsHeight,
  setDetailsHeight,
  dataSource,
  searchWithExpression,
  onRowSelected,
  tableColumns,
  tableData,
}) => {

  const table = <DataList dataViewerId={dataViewerId} />;

  const details = (
    <HorizontalPanel
      className="element-details__panel"
      // title={<div>{path(['feed'], tableData[selectedRow]) || 'Nothing selected'}</div>}
      onClose={() => deselectRow(dataViewerId)}
      content={
        <DetailsTabs
          data={dataForSelectedRow}
          details={detailsForSelectedRow}
          dataViewerId={dataViewerId}
        />
      }
      titleColumns={6}
      menuColumns={10}
      headerSize="h3"
    />
  );

  return (
    <React.Fragment>
      <div className="content-tabs__grid">
        <div className="data-viewer__header">
          <IconHeader icon="database" text="Data" />
          <ExpressionSearchBar
            className="data-viewer__search-bar"
            dataSource={dataSource}
            expressionId={dataViewerId}
            onSearch={() => {
              searchWithExpression(dataViewerId, pageOffset, pageSize, dataViewerId);
            }}
          />
        </div>
      </div>
      <div className="DataTable__container">
        <div className="DataTable__reactTable__container">
          {selectedRow === undefined ? (
            table
          ) : (
              <PanelGroup
                direction="column"
                panelWidths={[
                  {
                    resize: 'dynamic',
                    minSize: 100,
                    size: listHeight,
                  },
                  {
                    resize: 'dynamic',
                    minSize: 100,
                    size: detailsHeight,
                  },
                ]}
                onUpdate={(panelWidths) => {
                  setListHeight(panelWidths[0].size);
                  setDetailsHeight(panelWidths[1].size);
                }}
              >
                {table}
                {details}
              </PanelGroup>
            )}
        </div>
      </div>
    </React.Fragment>
  );
};

DataViewer.propTypes = {
  dataViewerId: PropTypes.string.isRequired,
};

export default enhance(DataViewer);
