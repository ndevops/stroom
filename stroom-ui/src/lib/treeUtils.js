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

/**
 * This function can be used to convert all the values inside an object using
 * a specified mapper function. The output object will have the same keys as the input
 * but with the mapped values.
 *
 * @param {object} input The input object
 * @param {function} mapper Mapping function to apply to each value in the object
 */
import uuidv4 from 'uuid/v4';

export function mapObject(input, mapper) {
  return Object.keys(input).reduce((previous, current) => {
    previous[current] = mapper(input[current]);
    return previous;
  }, {});
}

export function filterTree(treeNode, filterFunction) {
  const includeThisOne = filterFunction(treeNode);

  const filteredChildren = treeNode.children
    ? treeNode.children.map(c => filterTree(c, filterFunction)).filter(c => c !== undefined)
    : [];
  if (includeThisOne || filteredChildren.length > 0) {
    return {
      ...treeNode,
      children: filteredChildren,
    };
  }

  return undefined;
}

/**
 *
 * @param {treeNode} treeNode
 * @param {array of strings} uuids
 */
export function findByUuids(treeNode, uuids) {
  const results = [];

  iterateNodes(treeNode, (lineage, node) => {
    if (uuids.includes(node.uuid)) {
      results.push(node);
    }
  });

  return results;
}

/**
 * Recursively check that a tree node is inside the child hierarchy of the given destination tree node.
 *
 * @param {treeNode} treeNode The node we are looking into, this will be recursed through children
 * @param {treeNode} itemToFind The item to find
 */
export function itemIsInSubtree(treeNode, itemToFind) {
  if (treeNode.uuid === itemToFind.uuid) {
    return true;
  }

  if (treeNode.children) {
    return treeNode.children.filter(c => itemIsInSubtree(c, itemToFind)).length !== 0;
  }

  return false;
}

/**
 * Given an item, and a potential destination folder, this function determines if
 * it is valid for the item to move into that destination folder.
 * It will not be permitted if any of the following are true:
 * 1) A folder is being moved into itself
 * 2) A folder is being moved into one of it's sub folders (at any level of nesting)
 * 3) A document is being moved into the folder it's already in
 *
 * @param {treeNode} itemToMove The item that we may want to move
 * @param {treeNode} destinationFolder The potential destination folder
 */
export function canMove(itemToMove, destinationFolder) {
  // If the item being dropped is a folder, and is being dropped into itself
  if (itemIsInSubtree(itemToMove, destinationFolder)) {
    return false;
  }
  if (!!itemToMove.children && itemToMove.uuid === destinationFolder.uuid) {
    return false;
  }

  // Does this item appear in the destination folder already?
  return (
    destinationFolder.children.map(c => c.uuid).filter(u => u === itemToMove.uuid).length === 0
  );
}

/**
 * Used to assign UUID's to a tree of items, used where the UUID is only required for UI purposes.
 *
 * @param {treeNode} tree The input tree, will not be modified
 * @return {treeNode} the copied tree plus the UUID values
 */
export function assignRandomUuids(tree) {
  return tree
    ? {
      uuid: tree.uuid ? tree.uuid : uuidv4(), // assign a UUID if there isn't already one
      ...tree,
      children: tree.children ? tree.children.map(c => assignRandomUuids(c)) : undefined,
    }
    : undefined;
}

/**
 * Used to create a copy of a tree that has the UUID's stripped.
 * This is useful when the UUID's were only added for the UI in the first place.
 *
 * @param {treeNode} tree The input tree, will not be modified
 * @return {treeNode} the copied tree minus the UUID values
 */
export function stripUuids(tree) {
  return {
    ...tree,
    children: tree.children ? tree.children.map(c => stripUuids(c)) : undefined,
    uuid: undefined,
  };
}

/**
 * This function can be used to iterate through all nodes in a tree.
 * The callback can return 'true' if it wishes to skip any children from this node.
 *
 * @param {treeNode} tree The tree to iterate through
 * @param {function} callback Called for each node in the tree, first arg is an array of parents, second arg is the node
 * @param {array} lineage Contains the nodes in the lineage of the node given
 */
export function iterateNodes(tree, callback, lineage) {
  const thisLineage = lineage || [];

  const skipChildren = callback(thisLineage, tree);

  if (tree.children && !skipChildren) {
    tree.children.forEach(c => iterateNodes(c, callback, thisLineage.concat([tree])));
  }
}

/**
 * Picks a random item from the tree that passes the filter function.
 *
 * @param {treeNode} tree The tree through which to search
 * @param {function} filterFunction Callback that takes (lineage, node) and returns true/false for inclusion in random options
 * @return {object} Containing { node, lineage} of picked item
 */
export function pickRandomItem(tree, filterFunction) {
  const options = [];

  iterateNodes(tree, (lineage, node) => {
    if (filterFunction(lineage, node)) {
      options.push({ node, lineage });
    }
  });

  if (options.length > 0) {
    return options[Math.floor(Math.random() * options.length)];
  }

  return undefined;
}

/**
 * Given a tree, and a UUID, finds and returns the matching object and it's lineage
 *
 * @param {treeNode} tree
 * @param {string} uuid
 */
export function findItem(tree, uuid) {
  let foundNode,
    foundLineage;
  iterateNodes(tree, (lineage, node) => {
    if (node.uuid === uuid) {
      foundNode = node;
      foundLineage = lineage;
    }
  });

  if (foundNode && foundLineage) {
    return {
      node: foundNode,
      lineage: foundLineage,
    };
  }
  return undefined;
}

/**
 * Given a tree, this function returns a modified copy that applies the given updates to the node with a UUID
 * that matches the one given.
 *
 * @param {treeNode} treeNode Recursively applied to nodes in a tree, will be the root node in the first instance
 * @param {string} uuid The UUID of the node to modify
 * @param {object} updates The updates to apply to the matching node
 * @return {treeNode} The modified tree
 */
export function updateItemInTree(treeNode, uuid, updates) {
  let children;
  if (treeNode.children) {
    children = treeNode.children.map(x => updateItemInTree(x, uuid, updates));
  }

  let thisUpdates;
  if (uuid === treeNode.uuid) {
    thisUpdates = updates;
  }

  return {
    ...treeNode,
    ...thisUpdates,
    children,
  };
}

/**
 * Used to create a copy of an object with a particular key removed.
 * We can't use array filtering on objects.
 *
 * @param {object} input The input object
 * @param {string} key The key to remove
 */
export function deleteItemFromObject(input, key) {
  const output = {};

  Object.keys(input)
    .filter(k => k !== key)
    .forEach(k => (output[k] = input[k]));

  return output;
}

/**
 * Given a tree, this function returns a copy of the tree with the item removed with the matching UUID.
 *
 * @param {treeNode} treeNode The tree from which to remove the item.
 * @param {string} uuid The UUID of the item to delete
 *
 * @return {treeNode} Copy of the input tree with the input item removed.
 */
export function deleteItemFromTree(treeNode, uuid) {
  let children;
  if (treeNode.children) {
    children = treeNode.children.filter(x => x.uuid !== uuid).map(x => deleteItemFromTree(x, uuid));
  }

  return {
    ...treeNode,
    children,
  };
}

/**
 * Given a tree, this function returns a copy of the tree with the item removed with the matching UUID.
 *
 * @param {treeNode} treeNode The tree from which to remove the item.
 * @param {string} uuids The list of UUID of the item to delete
 *
 * @return {treeNode} Copy of the input tree with the input item removed.
 */
export function deleteItemsFromTree(treeNode, uuids) {
  let children;
  if (treeNode.children) {
    children = treeNode.children
      .filter(x => !uuids.includes(x.uuid))
      .map(x => deleteItemsFromTree(x, uuids));
  }

  return {
    ...treeNode,
    children,
  };
}

/**
 * Given a tree, returns a copy of the tree with the given items added the node with the matching UUID.
 *
 * @param {treeNode} treeNode
 * @param {string} parentUuid
 * @param {array of items} items
 */
export function addItemsToTree(treeNode, parentUuid, items) {
  let children;
  if (treeNode.children) {
    children = treeNode.children.map(x => addItemsToTree(x, parentUuid, items));
  }

  if (treeNode.uuid === parentUuid) {
    items.forEach((item) => {
      if (!item.uuid) {
        item.uuid = uuidv4();
      }
      children.push(item);
    });
  }

  return {
    ...treeNode,
    children,
  };
}

/**
 * Given a tree of data, returns a new tree with the item to move
 * relocated to the intended destination.
 *
 * @param {treeNode} rootNode The current entire tree of doc refs
 * @param {treeNode} itemToMove The item being moved
 * @param {treeNode} destination The destination tree node
 */
export function moveItemsInTree(rootNode, destination, itemsToMove) {
  let children;
  if (rootNode.children) {
    const itemsToInsert = rootNode.uuid === destination.uuid ? itemsToMove : []; // if this is the destination, insert the item to move
    const uuidsToMove = itemsToMove.map(i => i.uuid);
    const filteredItemsToCopy = rootNode.children.filter(c => !uuidsToMove.includes(c.uuid)); // remove the 'item to move'
    children = [
      ...itemsToInsert, // insert at beginning
      ...filteredItemsToCopy,
    ]
      .filter(c => !!c) // filter out undefined
      .map(c => moveItemsInTree(c, destination, itemsToMove)); // recurse children
  }

  return {
    ...rootNode,
    children,
  };
}

/**
 * Given a tree of data, returns a new tree with the item to move
 * relocated to the intended destination.
 *
 * @param {treeNode} rootNode The current entire tree of doc refs
 * @param {treeNode} itemToMove The item being moved
 * @param {treeNode} destination The destination tree node
 */
export function copyItemsInTree(rootNode, destination, itemsToCopy) {
  let children;
  if (rootNode.children) {
    const itemsToInsert = rootNode.uuid === destination.uuid ? itemsToCopy : []; // if this is the destination, insert the item to move
    children = [
      ...itemsToInsert, // insert at beginning
      ...rootNode.children,
    ]
      .filter(c => !!c) // filter out undefined
      .map(c => copyItemsInTree(c, destination, itemsToCopy)); // recurse children
  }

  return {
    ...rootNode,
    children,
  };
}
