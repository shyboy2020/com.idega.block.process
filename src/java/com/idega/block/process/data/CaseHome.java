package com.idega.block.process.data;


public interface CaseHome extends com.idega.data.IDOHome
{
 public Case create() throws javax.ejb.CreateException;
 public Case findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByGroup(com.idega.user.data.Group p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByUser(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByUser(com.idega.user.data.User p0,com.idega.block.process.data.CaseCode p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByUser(com.idega.user.data.User p0,com.idega.block.process.data.CaseCode p1,com.idega.block.process.data.CaseStatus p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByUser(com.idega.user.data.User p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesForGroupExceptCodes(com.idega.user.data.Group p0,com.idega.block.process.data.CaseCode[] p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesForGroupsAndUserExceptCodes(com.idega.user.data.User p0,java.util.Collection p1,com.idega.block.process.data.CaseCode[] p2,int p3,int p4)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesForUserExceptCodes(com.idega.user.data.User p0,com.idega.block.process.data.CaseCode[] p1,int p2,int p3)throws javax.ejb.FinderException;
 public java.util.Collection findSubCasesUnder(com.idega.block.process.data.Case p0)throws javax.ejb.FinderException;
 public int countSubCasesUnder(com.idega.block.process.data.Case p0);
 public java.lang.String getCaseStatusCancelled();
 public java.lang.String getCaseStatusContract();
 public java.lang.String getCaseStatusDenied();
 public java.lang.String getCaseStatusError();
 public java.lang.String getCaseStatusGranted();
 public java.lang.String getCaseStatusInactive();
 public java.lang.String getCaseStatusOpen();
 public java.lang.String getCaseStatusPreliminary();
 public java.lang.String getCaseStatusReady();
 public java.lang.String getCaseStatusRedeem();
 public java.lang.String getCaseStatusReview();
 public java.lang.String getCaseStatusMoved();
 public java.lang.String getCaseStatusPlaced();
 public int getNumberOfCasesByGroupsOrUserExceptCodes(com.idega.user.data.User p0,java.util.Collection p1,com.idega.block.process.data.CaseCode[] p2)throws com.idega.data.IDOException;
 public int getNumberOfCasesForUserExceptCodes(com.idega.user.data.User p0,com.idega.block.process.data.CaseCode[] p1)throws com.idega.data.IDOException;

}