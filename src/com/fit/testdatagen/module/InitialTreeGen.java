package com.fit.testdatagen.module;

import java.io.File;
import java.util.List;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdata.object.AbstractDataNode;
import com.fit.testdata.object.ClassDataNode;
import com.fit.testdata.object.EnumDataNode;
import com.fit.testdata.object.IAbstractDataNode;
import com.fit.testdata.object.NormalCharacterDataNode;
import com.fit.testdata.object.NormalNumberDataNode;
import com.fit.testdata.object.OneDimensionCharacterDataNode;
import com.fit.testdata.object.OneDimensionDataNode;
import com.fit.testdata.object.OneDimensionNumberDataNode;
import com.fit.testdata.object.OneDimensionStructureDataNode;
import com.fit.testdata.object.OneLevelCharacterDataNode;
import com.fit.testdata.object.OneLevelDataNode;
import com.fit.testdata.object.OneLevelNumberDataNode;
import com.fit.testdata.object.OneLevelStructureDataNode;
import com.fit.testdata.object.PointerDataNode;
import com.fit.testdata.object.RootDataNode;
import com.fit.testdata.object.StructDataNode;
import com.fit.testdata.object.StructureDataNode;
import com.fit.testdata.object.TwoDimensionCharacterDataNode;
import com.fit.testdata.object.TwoDimensionDataNode;
import com.fit.testdata.object.TwoDimensionNumberDataNode;
import com.fit.testdata.object.TwoDimensionStructureDataNode;
import com.fit.testdata.object.TwoLevelCharacterDataNode;
import com.fit.testdata.object.TwoLevelDataNode;
import com.fit.testdata.object.TwoLevelNumberDataNode;
import com.fit.testdata.object.TwoLevelStructureDataNode;
import com.fit.testdata.object.UnionDataNode;
import com.fit.testdatagen.testdatainit.VariableTypes;
import com.fit.tree.object.ClassNode;
import com.fit.tree.object.ExternalVariableNode;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.IVariableNode;
import com.fit.tree.object.StructNode;
import com.fit.tree.object.UnionNode;
import com.fit.tree.object.VariableNode;
import com.fit.utils.Utils;
import com.fit.utils.VariableTypesUtils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

/**
 * Táº¡o cÃ¢y khá»Ÿi Ä‘áº§u dá»±a trÃªn arguments vÃ  external variable Táº¥t cáº£ má»�i biáº¿n
 * truyá»�n vÃ o hÃ m thuá»™c ba loáº¡i:
 * <p>
 * + Biáº¿n cÆ¡ báº£n: Ä�Æ°á»£c sinh giÃ¡ trá»‹ ngáº«u nhiÃªn
 * <p>
 * + Biáº¿n máº£ng: máº·c Ä‘á»‹nh sá»‘ pháº§n tá»­ lÃ  0
 * <p>
 * + Biáº¿n con trá»�: máº·c Ä‘á»‹nh gÃ­a trá»‹ lÃ  null
 *
 * @author ducanh
 */
public class InitialTreeGen {
    private IFunctionNode functionNode;

    private IAbstractDataNode root;

    public InitialTreeGen() {
    }

    public static void main(String[] args) throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.TSDV_R1));

        String name = "SimpleMethodTest()";
//         String name = "StackLinkedList::push(Node*)";
        FunctionNode function = (FunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), name).get(0);

		/*
         *
		 */
        RootDataNode root = new RootDataNode();
        InitialTreeGen dataTreeGen = new InitialTreeGen();
        dataTreeGen.generateTree(root, function);
        System.out.println(new SimpleTreeDisplayer().toString(root));
    }

    public void generateTree(RootDataNode root, IFunctionNode functionNode) throws Exception {
        this.root = root;
        this.functionNode = functionNode;

        root.setName(functionNode.getSimpleName());

        List<IVariableNode> passingVariables = functionNode.getPassingVariables();
        for (INode passingVariable : passingVariables)
            genInitialTree((VariableNode) passingVariable, root);

        setVituralName(root);
    }

    private void genInitialTree(VariableNode vCurrentChild, AbstractDataNode nCurrentParent) throws Exception {
        String rawType = vCurrentChild.getRealType();
        if (rawType.contains("::"))
            rawType = rawType.split("::")[rawType.split("::").length - 1];

        if (VariableTypes.isBasic(rawType))
            initialBasicType(vCurrentChild, nCurrentParent);
        else if (VariableTypes.isOneLevel(rawType))
            initialOneLevelType(vCurrentChild, nCurrentParent);
        else if (VariableTypes.isOneDimension(rawType))
            initialOneDimensionType(vCurrentChild, nCurrentParent);
        else if (VariableTypes.isStructureSimple(rawType))
            if (VariableTypesUtils.isEnumNode(rawType))
                initialEnumType(vCurrentChild, nCurrentParent);
            else
                initialStructureType(vCurrentChild, nCurrentParent);
        else if (VariableTypes.isTwoDimension(rawType))
            initialTwoDimensionType(vCurrentChild, nCurrentParent);
        else if (VariableTypes.isTwoLevel(rawType))
            initialTwoLevelType(vCurrentChild, nCurrentParent);
        else
            throw new Exception("Loi khi xu ly " + vCurrentChild.toString());
    }

    public IFunctionNode getFunctionNode() {
        return functionNode;
    }

    public void setFunctionNode(FunctionNode functionNode) {
        this.functionNode = functionNode;
    }

    public IAbstractDataNode getRoot() {
        return root;
    }

    public void setRoot(IAbstractDataNode root) {
        this.root = root;
    }

    /**
     * Khá»Ÿi táº¡o biáº¿n truyá»�n vÃ o lÃ  kiá»ƒu cÆ¡ báº£n
     *
     * @param vParent
     * @param nParent
     */
    private void initialBasicType(VariableNode vParent, AbstractDataNode nParent) {
        AbstractDataNode child = null;
        if (VariableTypes.isCh(vParent.getRealType()))
            child = new NormalCharacterDataNode();
        else
            child = new NormalNumberDataNode();

        child.setChildren(null);
        child.setParent(nParent);
        child.setType(vParent.getRealType());
        child.setName(vParent.getNewType());
        child.setCorrespondingVar(vParent);
        if (vParent instanceof ExternalVariableNode)
            child.setExternelVariable(true);
        nParent.addChild(child);
    }

    /**
     * Khá»Ÿi táº¡o biáº¿n truyá»�n vÃ o lÃ  máº£ng má»™t chiá»�u
     *
     * @param vParent
     * @param nParent
     * @throws Exception
     */
    private void initialOneDimensionType(VariableNode vParent, AbstractDataNode nParent) throws Exception {
        String rawType = vParent.getRealType();
        AbstractDataNode child = null;
        if (VariableTypes.isCh(rawType))
            child = new OneDimensionCharacterDataNode();
        else if (VariableTypes.isNum(rawType))
            child = new OneDimensionNumberDataNode();
        else
            child = new OneDimensionStructureDataNode();
        /**
         *
         */
        child.setParent(nParent);
        child.setType(vParent.getRealType());
        child.setName(vParent.getNewType());
        child.setCorrespondingVar(vParent);
        ((OneDimensionDataNode) child).setSize(-1);
        if (vParent instanceof ExternalVariableNode)
            child.setExternelVariable(true);

        nParent.addChild(child);

    }

    /**
     * Khá»Ÿi táº¡o biáº¿n truyá»�n vÃ o lÃ  con trá»� má»™t má»©c
     *
     * @param vParent
     * @param nParent
     * @throws Exception
     */
    private void initialOneLevelType(VariableNode vParent, AbstractDataNode nParent) throws Exception {
        String rawType = Utils.getRealType(vParent.getReducedRawType(), vParent.getParent());
        AbstractDataNode child = null;
        if (VariableTypes.isCh(rawType))
            child = new OneLevelCharacterDataNode();
        else if (VariableTypes.isNum(rawType))
            child = new OneLevelNumberDataNode();
        else
            child = new OneLevelStructureDataNode();

        child.setParent(nParent);
        child.setType(vParent.getRealType());
        child.setName(vParent.getNewType());
        child.setCorrespondingVar(vParent);
        ((OneLevelDataNode) child).setAllocatedSize(PointerDataNode.NULL_VALUE);
        if (vParent instanceof ExternalVariableNode)
            child.setExternelVariable(true);

        nParent.addChild(child);
    }

    /**
     * Khá»Ÿi táº¡o biáº¿n truyá»�n vÃ o lÃ  kiá»ƒu cáº¥u trÃºc
     *
     * @param vParent
     * @param nParent
     * @throws Exception
     */
    private void initialStructureType(VariableNode vParent, AbstractDataNode nParent) throws Exception {
        INode correspondingNode = vParent.resolveCoreType();
        StructureDataNode child = null;

        if (correspondingNode instanceof StructNode)
            child = new StructDataNode();
        else if (correspondingNode instanceof ClassNode)
            child = new ClassDataNode();
        else if (correspondingNode instanceof UnionNode)
            child = new UnionDataNode();

        child.setParent(nParent);
        child.setName(vParent.getNewType());
        child.setType(vParent.getFullType());
        child.setCorrespondingVar(vParent);
        if (vParent instanceof ExternalVariableNode)
            child.setExternelVariable(true);
        nParent.addChild(child);
    }

    private void initialEnumType(VariableNode vParent, AbstractDataNode nParent) {
        EnumDataNode child = new EnumDataNode();

        child.setParent(nParent);
        child.setName(vParent.getNewType());
        child.setType(vParent.getRealType());
        child.setCorrespondingVar(vParent);
        if (vParent instanceof ExternalVariableNode)
            child.setExternelVariable(true);
        nParent.addChild(child);
    }

    /**
     * CÃ i tÃªn áº£o cho cÃ¢y
     *
     * @param n
     */
    private void setVituralName(IAbstractDataNode n) {
        if (n == null)
            return;
        else
            n.setVituralName(n.getName());
        if (n.getChildren() != null)
            for (IAbstractDataNode child : n.getChildren())
                setVituralName(child);
    }

    /**
     * Khá»Ÿi táº¡o biáº¿n truyá»�n vÃ o lÃ  máº£ng hai chiá»�u
     *
     * @param vParent
     * @param nParent
     * @throws Exception
     */
    private void initialTwoDimensionType(VariableNode vParent, AbstractDataNode nParent) throws Exception {
        String rawType = vParent.getRealType();
        AbstractDataNode child = null;
        if (VariableTypes.isChTwoDimension(rawType))
            child = new TwoDimensionCharacterDataNode();
        else if (VariableTypes.isNumTwoDimension(rawType))
            child = new TwoDimensionNumberDataNode();
        else
            child = new TwoDimensionStructureDataNode();
        /**
         *
         */
        child.setParent(nParent);
        child.setType(vParent.getRealType());
        child.setName(vParent.getNewType());
        child.setCorrespondingVar(vParent);
        ((TwoDimensionDataNode) child).setSizeA(-1);
        ((TwoDimensionDataNode) child).setSizeB(-1);
        if (vParent instanceof ExternalVariableNode)
            child.setExternelVariable(true);

        nParent.addChild(child);
    }

    /**
     * Khá»Ÿi táº¡o biáº¿n truyá»�n vÃ o lÃ  con trá»� hai má»©c
     *
     * @param vParent
     * @param nParent
     * @throws Exception
     */
    private void initialTwoLevelType(VariableNode vParent, AbstractDataNode nParent) throws Exception {
        String rawType = vParent.getRealType();
        AbstractDataNode child = null;
        if (VariableTypes.isChTwoLevel(rawType))
            child = new TwoLevelCharacterDataNode();
        else if (VariableTypes.isNumTwoLevel(rawType))
            child = new TwoLevelNumberDataNode();
        else
            child = new TwoLevelStructureDataNode();

        child.setParent(nParent);
        child.setType(vParent.getRealType());
        child.setName(vParent.getNewType());
        child.setCorrespondingVar(vParent);
        ((TwoLevelDataNode) child).setAllocatedSizeA(-1);
        ((TwoLevelDataNode) child).setAllocatedSizeB(-1);
        if (vParent instanceof ExternalVariableNode)
            child.setExternelVariable(true);

        nParent.addChild(child);
    }

}
