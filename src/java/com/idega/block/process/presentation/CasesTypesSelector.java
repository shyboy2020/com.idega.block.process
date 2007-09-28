package com.idega.block.process.presentation;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.block.process.business.CasesEngine;
import com.idega.block.process.business.ProcessConstants;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.SpringBeanLookup;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;

public class CasesTypesSelector extends IWBaseComponent {

	private String casesTypeSelectorStyle = "casesTypeSelectorStyle";
	
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		
		Layer container = new Layer();
		add(container);
		
		Text selectCaseType = new Text(getIWResourceBundle(iwc, ProcessConstants.IW_BUNDLE_IDENTIFIER).getLocalizedString("select_case_type", "Select case type: "));
		selectCaseType.setStyleClass("selectCaseTypeLabelStyle");
		container.add(selectCaseType);
		
		DropdownMenu casesTypesMenu = new DropdownMenu();
		casesTypesMenu.setStyleClass(casesTypeSelectorStyle);
		container.add(casesTypesMenu);
		addCasesTypes(iwc, casesTypesMenu);
	}
	
	@SuppressWarnings("unchecked")
	protected void updateComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		
		Layer container = null;
		try {
			container = (Layer) this.getChildren().get(0);
		} catch(Exception e) {
			return;
		}
		
		DropdownMenu casesTypesMenu = null;
		List<UIComponent> children = container.getChildren();
		if (children == null) {
			return;
		}
		UIComponent component = null;
		for (int i = 0; i < children.size(); i++) {
			component = children.get(i);
			if (component instanceof DropdownMenu) {
				casesTypesMenu = (DropdownMenu) component;
				if (!casesTypeSelectorStyle.equals(casesTypesMenu.getStyleClass())) {
					casesTypesMenu = null;
				}
			}
		}
		
		if (casesTypesMenu == null) {
			return;
		}
		addCasesTypes(iwc, casesTypesMenu);
	}
	
	private void addCasesTypes(IWContext iwc, DropdownMenu menu) {
		menu.removeElements();
		
		CasesEngine engine = (CasesEngine) SpringBeanLookup.getInstance().getSpringBean(iwc, CasesEngine.class);
		List<AdvancedProperty> types = engine.getCasesTypes(iwc, true);
		if (types == null) {
			menu.setDisabled(true);
			return;
		}
		if (types.size() == 0) {
			menu.setDisabled(true);
			return;
		}
		
		iwc.setApplicationAttribute(ProcessConstants.ACTIVE_PROCESS_DEFINITION, types.get(0).getId());
		
		AdvancedProperty type = null;
		for (int i = 0; i < types.size(); i++) {
			type = types.get(i);
			menu.addMenuElement(type.getId(), type.getValue());
		}
	}
	
	public Object saveState(FacesContext ctx) {
		Object values[] = new Object[2];
		values[0] = super.saveState(ctx);
		values[1] = casesTypeSelectorStyle;
		return values;
	}

	public void restoreState(FacesContext ctx, Object state) {
		Object values[] = (Object[]) state;
		casesTypeSelectorStyle = (String) values[1];
		super.restoreState(ctx, values[0]);
	}

}