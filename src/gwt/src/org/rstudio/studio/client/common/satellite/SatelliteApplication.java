/*
 * SatelliteApplication.java
 *
 * Copyright (C) 2009-12 by RStudio, Inc.
 *
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */
package org.rstudio.studio.client.common.satellite;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Provider;
import org.rstudio.core.client.CommandWithArg;
import org.rstudio.studio.client.application.ApplicationUncaughtExceptionHandler;
import org.rstudio.studio.client.workbench.views.source.editors.text.themes.AceThemes;

public class SatelliteApplication
{
   public SatelliteApplication(
                        String name,
                        SatelliteApplicationView view,
                        Satellite satellite,
                        Provider<AceThemes> pAceThemes,
                        ApplicationUncaughtExceptionHandler uncaughtExHandler)
   {
      name_ = name;
      view_ = view;
      satellite_ = satellite;
      pAceThemes_ = pAceThemes;
      uncaughtExHandler_ = uncaughtExHandler;
   }

   /**
    * Have subclasses override and return true if the satellite application is
    * not ready to process remote server events until some time after the
    * satellite window is created.
    * @return
    */
   protected boolean manuallyFlushPendingEvents()
   {
      return false;
   }
   
   public void go(RootLayoutPanel rootPanel, 
                  final Command dismissLoadingProgress)
   {
      // indicate that we are a satellite window
      satellite_.initialize(name_,
                            new CommandWithArg<JavaScriptObject> () {
                               @Override
                               public void execute(JavaScriptObject params)
                               {
                                  view_.reactivate(params);                
                               }
                            });

      if (!manuallyFlushPendingEvents())
      {
         flushPendingEvents();
      }
      
      // inject ace themes
      pAceThemes_.get();
      
      // register for uncaught exceptions (do this after calling 
      // initSatelliteWindow b/c it depends on Server)
      uncaughtExHandler_.register();
      
      // create the widget
      Widget w = view_.getWidget();
      rootPanel.add(w);
      rootPanel.setWidgetTopBottom(w, 0, Style.Unit.PX, 0, Style.Unit.PX);
      rootPanel.setWidgetLeftRight(w, 0, Style.Unit.PX, 0, Style.Unit.PX);
      
      // show the view
      view_.show(satellite_.getParams());
      
      // dismiss loading progress
      dismissLoadingProgress.execute();
   }

   protected void flushPendingEvents()
   {
      satellite_.flushPendingEvents(name_);
   }


   private final String name_;
   private final SatelliteApplicationView view_;
   private final Satellite satellite_;
   private final Provider<AceThemes> pAceThemes_;
   private final ApplicationUncaughtExceptionHandler uncaughtExHandler_;
}
