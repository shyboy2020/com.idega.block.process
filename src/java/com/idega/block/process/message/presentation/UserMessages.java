/*
 * $Id: UserMessages.java,v 1.12 2008/06/18 13:02:39 laddi Exp $
 * Created on Oct 13, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.process.message.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.process.message.data.Message;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.ListNavigator;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.text.Name;


/**
 * Last modified: $Date: 2008/06/18 13:02:39 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.12 $
 */
public class UserMessages extends MessageBlock implements IWPageEventListener {
	
	private String messageType;
	private ICPage iViewerPage;
	private int iMaxNumberOfEntries = -1;
	private int iNumberOfEntriesShown = -1;

	/* (non-Javadoc)
	 * @see com.idega.block.process.presentation.CaseBlock#present(com.idega.presentation.IWContext)
	 */
	@Override
	protected void present(IWContext iwc) throws Exception {
		if (getMessageType() == null) {
			add(new Text("No code set..."));
			return;
		}
		if (!iwc.isLoggedOn()) {
			add(new Text("No user logged on..."));
			return;
		}
		
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("caseElement");
		layer.setID("userMessages");
		
		Layer headerLayer = new Layer(Layer.DIV);
		headerLayer.setStyleClass("caseHeader");
		layer.add(headerLayer);
		
		headerLayer.add(new Heading1(getHeading()));

		Layer navigationLayer = new Layer(Layer.DIV);
		navigationLayer.setStyleClass("caseNavigation");
		headerLayer.add(navigationLayer);
		
		ListNavigator navigator = new ListNavigator("userMessages", getMessageCount(iwc));
		navigator.setFirstItemText(getResourceBundle().getLocalizedString("page", "Page") + ":");
		navigator.setDropdownEntryName(getResourceBundle().getLocalizedString("messages", "messages"));
		if (this.iNumberOfEntriesShown > 0) {
			navigator.setNumberOfEntriesPerPage(this.iNumberOfEntriesShown);
		}
		navigationLayer.add(navigator);
		
		layer.add(getCaseTable(iwc, navigator.getStartingEntry(iwc), getMaxNumberOfEntries() != -1 ? getMaxNumberOfEntries() : navigator.getNumberOfEntriesPerPage(iwc)));
		
		add(layer);
	}
	
	protected String getHeading() {
		return getResourceBundle().getLocalizedString("user_messages", "User messages");
	}
	
	private Table2 getCaseTable(IWContext iwc, int startingEntry, int numberOfEntries) throws RemoteException {
		Table2 table = new Table2();
		table.setStyleClass("caseTable");
		table.setStyleClass("ruler");
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		Collection cases = getMessages(iwc, startingEntry, numberOfEntries);

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setStyleClass("messageCount");
		cell.add(new Text(getResourceBundle().getLocalizedString("message_number", "Nr.")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("messageSubject");
		cell.add(new Text(getResourceBundle().getLocalizedString("subject", "Subject")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("messsageSender");
		cell.add(new Text(getResourceBundle().getLocalizedString("sender", "Sender")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("messageDate");
		cell.add(new Text(getResourceBundle().getLocalizedString("date", "Date")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setStyleClass("messageDelete");
		cell.add(new Text(getResourceBundle().getLocalizedString("delete", "Delete")));
		
		group = table.createBodyRowGroup();
		int iRow = 1;
		int messageNumber = startingEntry + 1;
		
		Iterator iter = cases.iterator();
		while (iter.hasNext()) {
			row = group.createRow();
			Message message = (Message) iter.next();
			if (iRow == 1) {
				row.setStyleClass("firstRow");
			}
			else if (!iter.hasNext()) {
				row.setStyleClass("lastRow");
			}
			if (!getMessageBusiness().isMessageRead(message)) {
				row.setStyleClass("newEntry");
			}
			User sender = message.getSender();
			IWTimestamp created = new IWTimestamp(message.getCreated());

			cell = row.createCell();
			cell.setStyleClass("firstColumn");
			cell.setStyleClass("messageCount");
			cell.add(new Text(String.valueOf(messageNumber)));
			
			cell = row.createCell();
			cell.setStyleClass("messageSubject");
			
			Link link = new Link(new Text(message.getSubject() != null ? message.getSubject() : getResourceBundle().getLocalizedString("message.no_subject", "No subject")));
			link.addParameter(PARAMETER_MESSAGE_PK, message.getPrimaryKey().toString());
			if (getViewerPage() != null) {
				link.setPage(getViewerPage());
			}
			else {
				link.setWindowToOpen(MessageWindow.class);
			}
			cell.add(link);

			cell = row.createCell();
			cell.setStyleClass("messsageSender");
			if (sender != null) {
				Name name = new Name(sender.getFirstName(), sender.getMiddleName(), sender.getLastName());
				cell.add(new Text(name.getName(iwc.getCurrentLocale())));
			}
			else {
				cell.add(new Text("-"));
			}

			cell = row.createCell();
			cell.setStyleClass("messageDate");
			cell.add(new Text(created.getLocaleDateAndTime(iwc.getCurrentLocale(), IWTimestamp.SHORT, IWTimestamp.SHORT)));
			
			cell = row.createCell();
			cell.setStyleClass("lastColumn");
			cell.setStyleClass("messageDelete");
			
			link = new Link(getBundle(iwc).getImage("delete.png", getResourceBundle().getLocalizedString("delete_message", "Delete message")));
			link.setStyleClass("deleteMessage");
			link.setEventListener(UserMessages.class);
			link.setOnClick("return confirm('" + getResourceBundle().getLocalizedString("delete_message_confirm", "Are you sure you want to delete this message?") + "');");
			link.setToolTip(getResourceBundle().getLocalizedString("delete_message", "Delete message"));
			link.addParameter(PARAMETER_MESSAGE_PK, message.getPrimaryKey().toString());
			cell.add(link);

			if (iRow % 2 == 0) {
				row.setStyleClass("evenRow");
			}
			else {
				row.setStyleClass("oddRow");
			}
			
			iRow++;
			messageNumber++;
		}
		
		return table;
	}

	protected Collection getMessages(IWContext iwc, int startingEntry, int numberOfEntries) {
		try {
			return getMessageBusiness().findMessages(this.messageType, iwc.getCurrentUser(), numberOfEntries, startingEntry);
		}
		catch (FinderException fe) {
			log(fe);
			return new ArrayList();
		}
		catch (RemoteException re) {
			log(re);
			return new ArrayList();
		}
	}
	
	protected int getMessageCount(IWContext iwc) {
		try {
			return getMessageBusiness().getNumberOfMessages(this.messageType, iwc.getCurrentUser());
		}
		catch (IDOException ie) {
			ie.printStackTrace();
			return 0;
		}
		catch (RemoteException re) {
			log(re);
			return 0;
		}
	}
	
	public void setViewerPage(ICPage page) {
		this.iViewerPage = page;
	}

	public boolean actionPerformed(IWContext iwc) throws IWException {
		if (iwc.isParameterSet(PARAMETER_MESSAGE_PK)) {
			try {
				String messagePKs = iwc.getParameter(PARAMETER_MESSAGE_PK);
				getMessageBusiness(iwc).deleteMessage(messagePKs);
				return true;
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		return false;
	}

	public void setMessageType(String type) {
		this.messageType = type;
	}
	
	public void setMaximumNumberOfEntries(int maxNumberOfEntries) {
		this.iMaxNumberOfEntries = maxNumberOfEntries;
	}

	
	protected int getMaxNumberOfEntries() {
		return this.iMaxNumberOfEntries;
	}

	
	protected ICPage getViewerPage() {
		return this.iViewerPage;
	}

	
	protected String getMessageType() {
		return this.messageType;
	}

	public void setNumberOfEntriesShownPerPage(int numberOfEntriesShown) {
		this.iNumberOfEntriesShown = numberOfEntriesShown;
	}
}
