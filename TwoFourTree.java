// Omar Alshafei

public class TwoFourTree {
    private class TwoFourTreeItem {
        int values = 1;
        int value1 = 0;                             // always exists.
        int value2 = 0;                             // exists iff the node is a 3-node or 4-node.
        int value3 = 0;                             // exists iff the node is a 4-node.
        boolean isLeaf = true;
        
        TwoFourTreeItem parent = null;              // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null;           // left and right child exist iff the note is a non-leaf.
        TwoFourTreeItem rightChild = null;          
        TwoFourTreeItem centerChild = null;         // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null;     // center-left and center-right children exist iff the node is a non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;

        public boolean isTwoNode() {
            return (values == 1);
        }

        public boolean isThreeNode() {
            return (values == 2);
        }

        public boolean isFourNode() {
            return (values == 3);
        }

        public boolean isRoot() {
            return (parent == null);
        }
        // one argument constructor for TwoFourTreeItem
        public TwoFourTreeItem(int value1) { 
            this.value1 = value1;
        }
        // two argument constructor TwoFourTreeItem
        public TwoFourTreeItem(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
            values = 2;
        }
        // three argument constructor TwoFourTreeItem
        public TwoFourTreeItem(int value1, int value2, int value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            values = 3;
        }

        private void printIndents(int indent) {
            for(int i = 0; i < indent; i++) System.out.printf("  ");
        }

        private void printInOrder(int indent) {
            if(!isLeaf) leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if(isThreeNode()) {
                if(!isLeaf) centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if(isFourNode()) {
                if(!isLeaf) centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if(!isLeaf) centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if(!isLeaf) rightChild.printInOrder(indent + 1);
        }
        // method to order values when adding a new value in node
        private void orderValues(int newValue) {
            // three node case
            if (isThreeNode()) {
                if (newValue < value1) {
                    value3 = value2;
                    value2 = value1;
                    value1 = newValue;
                } 
                else if (newValue > value2) {
                    value3 = newValue;
                } 
                else {
                    value3 = value2;
                    value2 = newValue;
                }
                values = 3;
            }
            // two node case
            else if (isTwoNode()) {
                if (newValue < value1) {
                    value2 = value1;
                    value1 = newValue;
                } 
                else {
                    value2 = newValue;
                }
                values = 2;
            }
        }
        // method to search through tree
        private boolean searchValue(int value) {
            TwoFourTreeItem node = this;
            // return true if value is found
            if (value == node.value1 || value == node.value2 || value == node.value3)
                return true;
            // call appropriate child for two node
            else if (node.isTwoNode()){
                if (value < node.value1 && node.leftChild != null) 
                    return node.leftChild.searchValue(value);
                        
                else if (node.rightChild != null) 
                    return node.rightChild.searchValue(value);

            }
            // call appropriate child for three node
            else if (node.isThreeNode()) {
                if (value < node.value1 && node.leftChild != null) 
                    return node.leftChild.searchValue(value);
                        
                else if (value > node.value2 && node.rightChild != null) 
                    return node.rightChild.searchValue(value);
                    
                else if (node.centerChild != null)
                    return node.centerChild.searchValue(value);
            }
            // call appropriate child for four node
            else if (node.isFourNode()) {
                if (value < node.value1 && node.leftChild != null) 
                    return node.leftChild.searchValue(value);
                            
                else if (value > node.value3 && node.rightChild != null)
                    return node.rightChild.searchValue(value);
                            
                else if (value > node.value2 && node.centerRightChild != null)
                    return node.centerRightChild.searchValue(value);
                            
                else if (node.centerLeftChild != null)
                    return node.centerLeftChild.searchValue(value);
            }
            // return false if value is not in the tree
            return false;
        }
        // split 4 node when adding into the tree
        private TwoFourTreeItem split() {
            TwoFourTreeItem newLeftNode = new TwoFourTreeItem(value1);
            TwoFourTreeItem newRightNode = new TwoFourTreeItem(value3);
            // reassign children
            newLeftNode.leftChild = leftChild;
            newLeftNode.rightChild = centerLeftChild;
            newRightNode.leftChild = centerRightChild;
            newRightNode.rightChild = rightChild;
            // set child parents and if not null then set node isLeaf to false
            if (leftChild != null){
                leftChild.parent = newLeftNode;
                newLeftNode.isLeaf = false;
            }
            if (centerLeftChild != null){
                centerLeftChild.parent = newLeftNode;
                newLeftNode.isLeaf = false;
            }
            if (centerRightChild != null){
                centerRightChild.parent = newRightNode;
                newRightNode.isLeaf = false;
            }
            if (rightChild != null) {
                rightChild.parent = newRightNode;
                newRightNode.isLeaf = false;
            }
            // special case for splitting root node
            if (isRoot()){
                root = new TwoFourTreeItem(value2);
                root.leftChild = newLeftNode;
                root.rightChild = newRightNode;
                newLeftNode.parent = root;
                newRightNode.parent = root;
                root.isLeaf = false;
                return root;
            }
            // if node is not root then merge
            else {
                newLeftNode.parent = parent;
                newRightNode.parent = parent;
                parent.orderValues(value2);
                parent.merge(this, newLeftNode, newRightNode);
                return parent;
            } 
        }
        // merge node after splitting
        private void merge(TwoFourTreeItem oldNode, TwoFourTreeItem lChild, TwoFourTreeItem rChild) {
            // merge three node
            if (isThreeNode()) { 
                if (oldNode == leftChild) {
                    leftChild = lChild;
                    centerChild = rChild;
                }
                else {
                    rightChild = rChild;
                    centerChild = lChild;
                }
            }
            // merge four node
            else if (isFourNode()) {
                if (oldNode == leftChild) {
                    leftChild = lChild;
                    centerLeftChild = rChild;
                    centerRightChild = centerChild;
                    centerChild = null;
                }
                else if (oldNode == centerChild) {
                    centerChild = null;
                    centerLeftChild = lChild;
                    centerRightChild = rChild;
                }
                else if (oldNode == rightChild){
                    rightChild = rChild;
                    centerRightChild = lChild;
                    centerLeftChild = centerChild;
                }
            }
        }
        // handles the proccess of adding a node in tree
        private TwoFourTreeItem insertValue(int value) {
            TwoFourTreeItem node = this;
            // if we come across a four node we split it
            if (node.isFourNode()) {
                node = split();
            }
            // adds value once a leaf node is reached
            if (node.isLeaf) {
                node.orderValues(value);
                return node;
            }
            // call appropriate child for two node
            else if (node.isTwoNode()) {
                if (value < node.value1)
                    return node.leftChild.insertValue(value);
                else
                    return node.rightChild.insertValue(value);
            } 
            // call appropriate child for three node
            else {
                if (value < value1)
                    return node.leftChild.insertValue(value);
                else if (value > value2)
                    return node.rightChild.insertValue(value);
                else
                    return node.centerChild.insertValue(value);
            }
        }
        // rotates or fuses two node during deletion
        private TwoFourTreeItem rotateOrFuse(){
            TwoFourTreeItem node = this;
            // 4 node parent case
            if (node.parent.isFourNode()){
                //rotate anticlockwise
                if(node == node.parent.leftChild && !node.parent.centerLeftChild.isTwoNode()){
                    int pVal = node.parent.value1;
                    node.value2 = pVal;
                    node.parent.value1 = node.parent.centerLeftChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.centerLeftChild.leftChild;
                    node.values = 2;

                    // fix R pointers
                    if(node.parent.centerLeftChild.isThreeNode()){
                        node.parent.centerLeftChild.value1 = node.parent.centerLeftChild.value2;
                        node.parent.centerLeftChild.value2 = 0;
                        node.parent.centerLeftChild.values = 1;
                        node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerChild;
                        node.parent.centerLeftChild.centerChild = null;
                    }
                    else if(node.parent.centerLeftChild.isFourNode()){
                        node.parent.centerLeftChild.value1 = node.parent.centerLeftChild.value2;
                        node.parent.centerLeftChild.value2 = node.parent.centerLeftChild.value3;
                        node.parent.centerLeftChild.value3 = 0;
                        node.parent.centerLeftChild.values = 2;                
                        node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerLeftChild;
                        node.parent.centerLeftChild.centerChild = node.parent.centerLeftChild.centerRightChild;
                        node.parent.centerLeftChild.centerLeftChild = null;
                        node.parent.centerLeftChild.centerRightChild = null;
                    }
                }
                //rotate anticlockwise
                else if(node == node.parent.centerLeftChild && !node.parent.centerRightChild.isTwoNode()){
                    int pVal = node.parent.value2;
                    node.value2 = pVal;
                    node.parent.value2 = node.parent.centerRightChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.centerRightChild.leftChild;
                    node.values = 2;

                    // fix pointers
                    if(node.parent.centerRightChild.isThreeNode()){
                        node.parent.centerRightChild.value1 = node.parent.centerRightChild.value2;
                        node.parent.centerRightChild.value2 = 0;
                        node.parent.centerRightChild.values = 1;   
                        node.parent.centerRightChild.leftChild = node.parent.centerRightChild.centerChild;
                        node.parent.centerRightChild.centerChild = null;
                    }
                    else if(node.parent.centerRightChild.isFourNode()){
                        node.parent.centerRightChild.value1 = node.parent.centerRightChild.value2;
                        node.parent.centerRightChild.value2 = node.parent.centerRightChild.value3;
                        node.parent.centerRightChild.value3 = 0;
                        node.parent.centerRightChild.values = 2;   
                        node.parent.centerRightChild.leftChild = node.parent.centerRightChild.centerLeftChild;
                        node.parent.centerRightChild.centerChild = node.parent.centerRightChild.centerRightChild;
                        node.parent.centerRightChild.centerLeftChild = null;
                        node.parent.centerRightChild.centerRightChild = null;
                    }
                }
                //rotate anticlockwise
                else if(node == node.parent.centerRightChild && !node.parent.rightChild.isTwoNode()){
                    int pVal = node.parent.value3;
                    node.value2 = pVal;
                    node.parent.value3 = node.parent.rightChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.rightChild.leftChild;
                    node.values = 2;

                    // fix pointers
                    if(node.parent.rightChild.isThreeNode()){
                        node.parent.rightChild.value1 = node.parent.rightChild.value2;
                        node.parent.rightChild.value2 = 0;
                        node.parent.rightChild.values = 1;
                        node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
                        node.parent.rightChild.centerChild = null;
                    }
                    else if(node.parent.rightChild.isFourNode()){
                        node.parent.rightChild.value1 = node.parent.rightChild.value2;
                        node.parent.rightChild.value2 = node.parent.rightChild.value3;
                        node.parent.rightChild.value3 = 0;
                        node.parent.rightChild.values = 2;
                        node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
                        node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
                        node.parent.rightChild.centerLeftChild = null;
                        node.parent.rightChild.centerRightChild = null;
                    }
                }
                //rotate clockwise
                else if(node == node.parent.centerLeftChild && !node.parent.leftChild.isTwoNode()){
                    int pVal = node.parent.value1;
                    node.value2 = node.value1;
                    node.value1 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.leftChild.rightChild;
                    node.values = 2;

                    // fix pointers
                    if(node.parent.leftChild.isThreeNode()){
                        node.parent.value1 = node.parent.leftChild.value2;
                        node.parent.leftChild.value2 = 0;
                        node.parent.leftChild.values = 1;
                        node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
                        node.parent.leftChild.centerChild = null;
                    }
                    else if(node.parent.leftChild.isFourNode()){
                        node.parent.value1 = node.parent.leftChild.value3;
                        node.parent.leftChild.value3 = 0;
                        node.parent.leftChild.values = 1;
                        node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
                        node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
                        node.parent.leftChild.centerLeftChild = null;
                        node.parent.leftChild.centerRightChild = null;
                    }
                }
                //rotate clockwise
                else if(node == node.parent.centerRightChild && !node.parent.centerLeftChild.isTwoNode()){
                    int pVal = node.parent.value2;
                    node.value2 = node.value1;
                    node.value1 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.centerLeftChild.rightChild;
                    node.values = 2;

                    // fix pointers
                    if(node.parent.centerLeftChild.isThreeNode()){
                        node.parent.value2 = node.parent.centerLeftChild.value2;
                        node.parent.centerLeftChild.value2 = 0;
                        node.parent.centerLeftChild.values = 1;
                        node.parent.centerLeftChild.rightChild = node.parent.centerLeftChild.centerChild;
                        node.parent.centerLeftChild.centerChild = null;
                    }
                    else if(node.parent.centerLeftChild.isFourNode()){
                        node.parent.value2 = node.parent.leftChild.value3;
                        node.parent.centerLeftChild.value3 = 0;
                        node.parent.centerLeftChild.values = 2;
                        node.parent.centerLeftChild.centerChild = node.parent.centerLeftChild.centerLeftChild;
                        node.parent.centerLeftChild.rightChild = node.parent.centerLeftChild.centerRightChild;
                        node.parent.centerLeftChild.centerLeftChild = null;
                        node.parent.centerLeftChild.centerRightChild = null;
                    }
                }
                //rotate clockwise
                else if(node == node.parent.rightChild && !node.parent.centerRightChild.isTwoNode()){
                    int pVal = node.parent.value3;
                    node.value2 = node.value1;
                    node.value1 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.centerRightChild.rightChild;
                    node.values = 2;

                    // fix pointers
                    if(node.parent.centerRightChild.isThreeNode()){
                        node.parent.value3 = node.parent.centerRightChild.value2;
                        node.parent.centerRightChild.value2 = 0;
                        node.parent.centerRightChild.values = 1;
                        node.parent.centerRightChild.rightChild = node.parent.centerRightChild.centerChild;
                        node.parent.centerRightChild.centerChild = null;
                    }
                    else if(node.parent.centerRightChild.isFourNode()){
                        node.parent.value3 = node.parent.centerRightChild.value3;
                        node.parent.centerRightChild.value3 = 0;
                        node.parent.centerRightChild.values = 2;
                        node.parent.centerRightChild.centerChild = node.parent.centerRightChild.centerLeftChild;
                        node.parent.centerRightChild.rightChild = node.parent.centerRightChild.centerRightChild;
                        node.parent.centerRightChild.centerLeftChild = null;
                        node.parent.centerRightChild.centerRightChild = null;
                    }
                }
                // if node did not rotate then it has 2 node immediate siblings therefore we must fuse
                else{
                    // fuse leftChild
                    if(node == node.parent.leftChild){
                        node.value2 = node.parent.value1;
                        node.value3 = node.parent.centerLeftChild.value1;
                        node.values = 3;
                        node.parent.value1 = node.parent.value2;
                        node.parent.value2 = node.parent.value3;
                        node.parent.value3 = 0;
                        node.parent.values = 2;
                        node.centerLeftChild = node.rightChild;
                        node.centerRightChild = node.parent.centerLeftChild.leftChild;
                        node.rightChild = node.parent.centerLeftChild.rightChild;
                        node.parent.centerChild = node.parent.centerRightChild;
                        node.parent.centerLeftChild = null;
                        node.parent.centerRightChild = null;
                    }
                    // fuse centerLeftChild or centerRightChild
                    else if(node == node.parent.centerLeftChild || node == node.parent.centerRightChild){
                        node = node.parent.centerLeftChild;
                        node.value2 = node.parent.value2;
                        node.parent.value2 = node.parent.value3;
                        node.parent.value3 = 0;
                        node.parent.values--;
                        node.value3 = node.parent.centerRightChild.value1;
                        node.centerLeftChild = node.rightChild;
                        node.centerRightChild = node.parent.centerRightChild.leftChild;
                        node.rightChild = node.parent.centerRightChild.rightChild;
                        node.parent.centerChild = node;
                        node.parent.centerLeftChild = null;
                        node.parent.centerRightChild = null;
                    }
                    // fuse rightChild
                    else if(node == node.parent.rightChild){
                        node.value3 = node.value1;
                        node.value2 = node.parent.value3;
                        node.value1 = node.parent.centerRightChild.value1;
                        node.values = 3;
                        node.parent.value3 = 0;
                        node.parent.values--;;
                        node.centerRightChild = node.leftChild;
                        node.centerLeftChild = node.parent.centerRightChild.rightChild;
                        node.leftChild = node.parent.centerRightChild.leftChild;             
                        node.parent.centerChild = node.parent.centerLeftChild;
                        node.parent.centerLeftChild = null;
                        node.parent.centerRightChild = null;
                    }
                }
            }
            // 3 node parent case
            else if (node.parent.isThreeNode()){
                //rotate anticlockwise
                if(node == node.parent.leftChild && !node.parent.centerChild.isTwoNode()){
                    int pVal = node.parent.value1;
                    node.value2 = pVal;
                    node.parent.value1 = node.parent.centerChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.centerChild.leftChild;
                    node.values = 2;


                    // fix pointers
                    if(node.parent.centerChild.isThreeNode()){
                        node.parent.centerChild.value1 = node.parent.centerChild.value2;
                        node.parent.centerChild.value2 = 0;
                        node.parent.centerChild.values = 1; 
                        node.parent.centerChild.leftChild = node.parent.centerChild.centerChild;
                        node.parent.centerChild.centerChild = null;
                    }
                    else if(node.parent.centerChild.isFourNode()){
                        node.parent.centerChild.value1 = node.parent.centerChild.value2;
                        node.parent.centerChild.value2 = node.parent.centerChild.value3;
                        node.parent.centerChild.value3 = 0;
                        node.parent.centerChild.values = 2;   
                        node.parent.centerChild.leftChild = node.parent.centerChild.centerLeftChild;
                        node.parent.centerChild.centerChild = node.parent.centerChild.centerRightChild;
                        node.parent.centerChild.centerLeftChild = null;
                        node.parent.centerChild.centerRightChild = null;
                    }
                }
                //rotate anticlockwise
                else if(node == node.parent.centerChild && !node.parent.rightChild.isTwoNode()){
                    int pVal = node.parent.value2;
                    node.value2 = pVal;
                    node.parent.value2 = node.parent.rightChild.value1;
                    node.centerChild = node.rightChild;
                    node.rightChild = node.parent.rightChild.leftChild;
                    node.values = 2;

                    // fix pointers
                    if(node.parent.rightChild.isThreeNode()){
                        node.parent.rightChild.value1 = node.parent.rightChild.value2;
                        node.parent.rightChild.value2 = 0;
                        node.parent.rightChild.values = 1;   
                        node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
                        node.parent.rightChild.centerChild = null;
                    }
                    else if(node.parent.rightChild.isFourNode()){
                        node.parent.rightChild.value1 = node.parent.rightChild.value2;
                        node.parent.rightChild.value2 = node.parent.rightChild.value3;
                        node.parent.rightChild.value3 = 0;
                        node.parent.rightChild.values = 2;   
                        node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
                        node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
                        node.parent.rightChild.centerLeftChild = null;
                        node.parent.rightChild.centerRightChild = null;
                    }
                }
                //rotate clockwise
                else if(node == node.parent.centerChild && !node.parent.leftChild.isTwoNode()){
                    int pVal = node.parent.value1;
                    node.value2 = node.value1;
                    node.value1 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.leftChild.rightChild;
                    node.values = 2;

                    // fix pointers
                    if(node.parent.leftChild.isThreeNode()){
                        node.parent.value1 = node.parent.leftChild.value2;
                        node.parent.leftChild.value2 = 0;
                        node.parent.leftChild.values = 1;  
                        node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
                        node.parent.leftChild.centerChild = null;
                    }
                    else if(node.parent.leftChild.isFourNode()){
                        node.parent.value1 = node.parent.leftChild.value3;
                        node.parent.leftChild.value3 = 0;
                        node.parent.leftChild.values = 2;  
                        node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
                        node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
                        node.parent.leftChild.centerLeftChild = null;
                        node.parent.leftChild.centerRightChild = null;
                    }
                }
                //rotate clockwise
                else if(node == node.parent.rightChild && !node.parent.centerChild.isTwoNode()){
                    int pVal = node.parent.value2;
                    node.value2 = node.value1;
                    node.value1 = pVal;
                    node.centerChild = node.leftChild;
                    node.leftChild = node.parent.centerChild.rightChild;
                    node.values = 2;

                    // fix pointers
                    if(node.parent.centerChild.isThreeNode()){
                        node.parent.value2 = node.parent.leftChild.value2;
                        node.parent.centerChild.value2 = 0;
                        node.parent.centerChild.values = 1;  
                        node.parent.centerChild.rightChild = node.parent.centerChild.centerChild;
                        node.parent.centerChild.centerChild = null;
                    }
                    else if(node.parent.centerChild.isFourNode()){
                        node.parent.value2 = node.parent.leftChild.value3;
                        node.parent.centerChild.value3 = 0;
                        node.parent.centerChild.values = 2; 
                        node.parent.centerChild.centerChild = node.parent.centerChild.centerLeftChild;
                        node.parent.centerChild.rightChild = node.parent.centerChild.centerRightChild;
                        node.parent.centerChild.centerLeftChild = null;
                        node.parent.centerChild.centerRightChild = null;
                    }
                }
                // if node did not rotate then it has 2 node immediate siblings therefore we must fuse
                else{
                    // fuse leftChild
                    if(node == node.parent.leftChild){
                        node.value2 = node.parent.value1;
                        node.value3 = node.parent.centerChild.value1;
                        node.values = 3;
                        
                        node.parent.value1 = node.parent.value2;
                        node.parent.value2 = 0;
                        node.parent.values = 1;
                        
                        node.centerLeftChild = node.rightChild;
                        node.centerRightChild = node.parent.centerChild.leftChild;
                        node.rightChild = node.parent.centerChild.rightChild;
                        
                        node.parent.centerChild = null;
                        
                    }
                    // fuse centerChild or rightChild
                    else if(node == node.parent.centerChild || node == node.parent.rightChild){
                        node = node.parent.centerChild;
                        node.value2 = node.parent.value2;
                        node.value3 = node.parent.rightChild.value1;
                        node.values = 3;
                        
                        node.parent.value2 = 0;
                        node.parent.values = 1;
                        
                        node.centerLeftChild = node.rightChild;
                        node.centerRightChild = node.parent.rightChild.leftChild;
                        node.rightChild = node.parent.rightChild.rightChild;
                    
                        node.parent.rightChild = node;
                        node.parent.centerChild = null;
                    }
                }
            }
            // fix children parent pointers
            if (node.leftChild != null){
                node.leftChild.parent = node;
                node.isLeaf = false;
            }
            if (node.centerLeftChild != null){
                node.centerLeftChild.parent = node;
                node.isLeaf = false;
            }
            if (node.centerRightChild != null){
                node.centerRightChild.parent = node;
                node.isLeaf = false;
            }
            if (node.rightChild != null) {
                node.rightChild.parent = node;
                node.isLeaf = false;
            }
            return node;
        }
        // handles deletion once value is found in a node
        private void deleteCases(int value){ 
            TwoFourTreeItem node = this;
            if (node.isTwoNode() && !node.parent.isTwoNode())
                node = node.rotateOrFuse();
            // delete directly if value is in a leaf node
            if(node.isLeaf && !node.isTwoNode()){
                if (value == node.value1){
                    node.value1 = node.value2;
                    node.value2 = node.value3;
                    node.value3 = 0;
                    node.values--;
                }
                else if (value == node.value2){
                    node.value2 = node.value3;
                    node.value3 = 0;
                    node.values--;
                }
                else if (value == node.value3){
                    node.value3 = 0;
                    node.values--;
                }
            }
            // if value is not in a leaf node swap it with its leftmost right descendent
            else if (!node.isLeaf && !node.isTwoNode()){
                TwoFourTreeItem temp = node;
                // Value 1
                if (value == node.value1){
                    if (node.isFourNode())
                        temp = node.centerLeftChild;
                    else if (node.isThreeNode())
                        temp = node.centerChild;
                    while(!temp.isLeaf){
                        if (temp.isTwoNode() && !temp.parent.isTwoNode())
                            temp = temp.rotateOrFuse();
                        temp = temp.leftChild;
                    }
                    node.value1 = temp.value1;
                }
                // Value 2
                else if (value == node.value2){
                    if (node.isFourNode())
                        temp = node.centerRightChild;
                    else if (node.isThreeNode())
                        temp = node.rightChild;
                    while(!temp.isLeaf){
                        if (temp.isTwoNode() && !temp.parent.isTwoNode())
                            temp = temp.rotateOrFuse();
                        temp = temp.leftChild;
                    }
                    node.value2 = temp.value1;
                }
                // Value 3
                else if (value == node.value3){
                    temp = node.rightChild;
                    while(!temp.isLeaf){
                        if (temp.isTwoNode() && !temp.parent.isTwoNode())
                            temp = temp.rotateOrFuse();
                        temp = temp.leftChild;
                    }
                    node.value3 = temp.value1;
                }

                // swap values and recall method with the value in the leaf node
                temp.value1 = value;
                temp.deleteCases(value);
            }
        }
        // handles the proccess of deletion in a node
        private void delete(int value){
            TwoFourTreeItem node = this;
            while(hasValue(value)){
                // if node is a two node we must operate on the node
                if(node.isTwoNode()){
                    // cases if the node is the root
                    if(node.isRoot()){
                        if(node.rightChild.isTwoNode() && !node.leftChild.isTwoNode() && value > node.value1)
                            node = rootRightChild();
                        
                        else if(node.leftChild.isTwoNode() && !node.rightChild.isTwoNode() && value < node.value1)
                            node = rootLeftChild();
                    }
                    // if it is not the root we can rotate or fuse the node
                    else if (node.isTwoNode() && !node.parent.isTwoNode())
                        node = node.rotateOrFuse();
                }
                // if value is found in node then call method to delete the value
                if ((value == node.value1 || value == node.value2 || value == node.value3) && !node.isTwoNode()){
                    node.deleteCases(value);
                    break;
                }
                // traverse tree if node is not a leaf
                else if (!node.isLeaf){
                    if (node.isTwoNode()){
                        if(node.isRoot()){
                            if (value < node.value1)
                                node = node.leftChild;
                            else
                                node = node.rightChild;
                        }
                        else if (node.isTwoNode() && !node.parent.isTwoNode())
                            node = node.rotateOrFuse();
                    }
                    else if (node.isThreeNode()){
                        if (value < node.value1)
                            node = node.leftChild;
                        else if (value > node.value2)
                            node = node.rightChild;
                        else
                            node = node.centerChild;
                    }
                    else if (node.isFourNode()){
                        if (value < node.value1)
                            node = node.leftChild;
                        else if (value > node.value3)
                            node = node.rightChild;
                        else if (value > node.value2)
                            node = node.centerRightChild;
                        else
                            node = node.centerLeftChild;
                    }
                }
            }
        }
        // fuses root if it and its children and 2 nodes
        private TwoFourTreeItem fixRoot(){
            TwoFourTreeItem node = this;
            TwoFourTreeItem newRoot = new TwoFourTreeItem(node.leftChild.value1, node.value1, node.rightChild.value1);
            newRoot.parent = node.parent;
            // setting the new root's children and children parents
            newRoot.leftChild = node.leftChild.leftChild;
            if(newRoot.leftChild != null) 
                newRoot.leftChild.parent = newRoot;
            
            newRoot.centerLeftChild = node.leftChild.rightChild;
            if(newRoot.centerLeftChild != null) 
                newRoot.centerLeftChild.parent = newRoot;
                
            newRoot.centerRightChild = node.rightChild.leftChild;
            if(newRoot.centerRightChild != null) 
                newRoot.centerRightChild.parent = newRoot;
                
            newRoot.rightChild = node.rightChild.rightChild;
            if(newRoot.rightChild != null) 
                newRoot.rightChild.parent = newRoot;
                
            if(newRoot.leftChild != null || newRoot.centerLeftChild != null || newRoot.centerRightChild != null || newRoot.rightChild != null) 
                newRoot.isLeaf = false;
                
            return newRoot;
        }
        // case for when root and left child are 2 nodes but right child is not
        private TwoFourTreeItem rootLeftChild(){
            TwoFourTreeItem node = root.leftChild;
            node.value2 = node.parent.value1;
            node.values = 2;
            node.parent.value1 = node.parent.rightChild.value1;
            node.centerChild = node.rightChild;
            node.rightChild = node.parent.rightChild.leftChild;

            node.parent.rightChild.value1 = node.parent.rightChild.value2;
            node.parent.rightChild.value2 = node.parent.rightChild.value3;
            node.parent.rightChild.value3 = 0;
                    
            if(node.parent.rightChild.isThreeNode()){
                node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
                node.parent.rightChild.centerChild = null;
            }
            if(node.parent.rightChild.isFourNode()){
                node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
                node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
                node.parent.rightChild.centerLeftChild = null;
                node.parent.rightChild.centerRightChild = null;
            }
            node.parent.rightChild.values--;
            return node;
        }
        // case for when root and right child are 2 nodes but left child is not
        private TwoFourTreeItem rootRightChild(){
            TwoFourTreeItem node = root.rightChild;
            node.value2 = node.value1;
            node.value1 = node.parent.value1;
            node.values = 2;
            node.parent.leftChild.value1 = node.parent.leftChild.value2;
            node.parent.leftChild.value2 = node.parent.leftChild.value3;
            node.centerChild = node.leftChild;
            node.leftChild = node.parent.leftChild.rightChild;
            
            if(node.parent.leftChild.isThreeNode()){
                node.parent.value1 = node.parent.leftChild.value2;
                node.parent.leftChild.value2 = 0;
                node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
                node.parent.leftChild.centerChild = null;
            }
            if(node.parent.leftChild.isFourNode()){
                node.parent.value1 = node.parent.leftChild.value3;
                node.parent.leftChild.value3 = 0;
                node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
                node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
                node.parent.leftChild.centerLeftChild = null;
                node.parent.leftChild.centerRightChild = null;
            }
            node.parent.leftChild.values--;
            return node;
        }
    }
    TwoFourTreeItem root = null;
    // adds value if it is not already in the tree
    public boolean addValue(int value) {
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return true;
        }
        else if(hasValue(value))
            return false;
        // insert value in tree
        root.insertValue(value);
        return true;
        
    }
    // checks if value is in the tree
    public boolean hasValue(int value) {
        return root.searchValue(value);
    }
    
    // deletes value if it is in the tree
    public boolean deleteValue(int value) {
        if(root == null)
            return false;
            
        else if (!hasValue(value))
            return false;
        // check if root is a two node and both its children are also two node
        if(root.isTwoNode()){
            if(root.leftChild != null && root.rightChild != null) {
                // call fuse root method if true
                if(root.leftChild.isTwoNode() && root.rightChild.isTwoNode())
                root = root.fixRoot();
            }
        }
        // delete value from tree
        root.delete(value);
        
        return true;
    }

    public void printInOrder() {
        if(root != null) root.printInOrder(0);
    }

    public TwoFourTree() {
        
    }
}