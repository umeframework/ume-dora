///*
// * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
// */
//package org.umeframework.dora.transaction.impl;
//
//import java.util.Stack;
//
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.support.AbstractPlatformTransactionManager;
//import org.springframework.transaction.support.DefaultTransactionDefinition;
//import org.umeframework.dora.context.SessionContext;
//import org.umeframework.dora.exception.DataAccessException;
//import org.umeframework.dora.transaction.TransactionManager;
//
///**
// * Transaction manager implementation base on spring AbstractPlatformTransactionManager instance.
// *
// * @author Yue MA
// *
// */
//public class TransactionManagerImpl implements TransactionManager {
//    /**
//     * TransactionStatusKey
//     */
//    private static final String TransactionStatusKey = TransactionStatus.class.getName() + Stack.class.getSimpleName();
//    /**
//     * Transaction propagation
//     */
//    private int propagation = TransactionDefinition.PROPAGATION_REQUIRED;
//    /**
//     * Actual transaction manager instance
//     */
//    private AbstractPlatformTransactionManager platformTransactionManager;
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see org.umeframework.dora.transaction.TransactionManager#begin()
//     */
//    @Override
//    public void begin() {
//        begin(propagation);
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see org.umeframework.dora.transaction.TransactionManager#begin(int)
//     */
//    @Override
//    public void begin(int propagation) {
//        try {
//            Stack<TransactionStatus> txStatusStack = SessionContext.open().getData(TransactionStatusKey);
//            if (txStatusStack == null) {
//                txStatusStack = new Stack<TransactionStatus>();
//            }
//            TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition(propagation));
//
//            txStatusStack.push(transactionStatus);
//            SessionContext.open().setData(TransactionStatusKey, txStatusStack);
//        } catch (Exception e) {
//            throw new DataAccessException(e, "Failed to begin transaction.");
//        }
//    }
//    /*
//     * (non-Javadoc)
//     * 
//     * @see org.umeframework.dora.transaction.TransactionManager#commit()
//     */
//    @Override
//    public void commit() {
//        try {
//            Stack<TransactionStatus> txStatusStack = SessionContext.open().getData(TransactionStatusKey);
//            if (txStatusStack != null) {
//                TransactionStatus transactionStatus = txStatusStack.pop();
//                platformTransactionManager.commit(transactionStatus);
//            }
//        } catch (Exception e) {
//            throw new DataAccessException(e, "Transaction commit error.");
//        }
//
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see org.umeframework.dora.transaction.TransactionManager#rollback()
//     */
//    @Override
//    public void rollback() {
//        try {
//            Stack<TransactionStatus> txStatusStack = SessionContext.open().getData(TransactionStatusKey);
//            if (txStatusStack != null) {
//                TransactionStatus transactionStatus = txStatusStack.pop();
//                platformTransactionManager.rollback(transactionStatus);
//            }
//        } catch (Exception e) {
//            throw new DataAccessException(e, "Transaction rollback error.");
//        }
//
//    }
//
//    /**
//     * @return the propagation
//     */
//    public int getPropagation() {
//        return propagation;
//    }
//
//    /**
//     * @param propagation
//     *            the propagation to set
//     */
//    public void setPropagation(int propagation) {
//        this.propagation = propagation;
//    }
//
//    /**
//     * @return the platformTransactionManager
//     */
//    public AbstractPlatformTransactionManager getPlatformTransactionManager() {
//        return platformTransactionManager;
//    }
//
//    /**
//     * @param platformTransactionManager
//     *            the platformTransactionManager to set
//     */
//    public void setPlatformTransactionManager(AbstractPlatformTransactionManager platformTransactionManager) {
//        this.platformTransactionManager = platformTransactionManager;
//    }
//
//}
