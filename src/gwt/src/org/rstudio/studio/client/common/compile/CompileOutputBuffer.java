/*
 * CompileOutputBuffer.java
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


package org.rstudio.studio.client.common.compile;

import org.rstudio.core.client.VirtualConsole;
import org.rstudio.core.client.widget.BottomScrollPanel;
import org.rstudio.core.client.widget.FontSizer;
import org.rstudio.core.client.widget.PreWidget;
import org.rstudio.studio.client.workbench.views.console.ConsoleResources;

import com.google.gwt.user.client.ui.Composite;

public class CompileOutputBuffer extends Composite 
                                 implements CompileOutputDisplay
{
   public CompileOutputBuffer()
   {
      output_ = new PreWidget();
      output_.setStylePrimaryName(
                        ConsoleResources.INSTANCE.consoleStyles().output());
      FontSizer.applyNormalFontSize(output_);
    
      scrollPanel_ = new BottomScrollPanel();
      scrollPanel_.setSize("100%", "100%");
      scrollPanel_.add(output_);
      
      initWidget(scrollPanel_);
   }
   
   public void append(String output)
   {
      virtualConsole_.submit(output);
      output_.setText(virtualConsole_.toString()); 

      scrollPanel_.onContentSizeChanged();
   }
   
   @Override
   public void writeCommand(String command)
   {
      append(command);
      
   }

   @Override
   public void writeOutput(String output)
   {
      append(output);
      
   }

   @Override
   public void writeError(String error)
   {
      append(error);
   }
   
   @Override
   public void scrollToBottom()
   {
     scrollPanel_.scrollToBottom();
   }

   @Override
   public void clear()
   {
      output_.setText("");
      virtualConsole_ = new VirtualConsole();
   }
 
   private PreWidget output_;
   private VirtualConsole virtualConsole_ = new VirtualConsole();
   private BottomScrollPanel scrollPanel_;
}
