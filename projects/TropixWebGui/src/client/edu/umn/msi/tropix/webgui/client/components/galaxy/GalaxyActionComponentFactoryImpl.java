/*******************************************************************************
 * Copyright 2009 Regents of the University of Minnesota. All rights
 * reserved.
 * Copyright 2009 Mayo Foundation for Medical Education and Research.
 * All rights reserved.
 *
 * This program is made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED INCLUDING, WITHOUT LIMITATION, ANY WARRANTIES OR CONDITIONS
 * OF TITLE, NON-INFRINGEMENT, MERCHANTABILITY OR FITNESS FOR A
 * PARTICULAR PURPOSE.  See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * Contributors:
 * Minnesota Supercomputing Institute - initial API and implementation
 ******************************************************************************/

package edu.umn.msi.tropix.webgui.client.components.galaxy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.TreeGrid;

import edu.umn.msi.tropix.galaxy.inputs.Input;
import edu.umn.msi.tropix.galaxy.inputs.RootInput;
import edu.umn.msi.tropix.galaxy.tool.Data;
import edu.umn.msi.tropix.galaxy.tool.InputType;
import edu.umn.msi.tropix.galaxy.tool.Outputs;
import edu.umn.msi.tropix.galaxy.tool.Param;
import edu.umn.msi.tropix.galaxy.tool.ParamOption;
import edu.umn.msi.tropix.galaxy.tool.ParamType;
import edu.umn.msi.tropix.galaxy.tool.Tool;
import edu.umn.msi.tropix.jobs.activities.descriptions.ActivityDescription;
import edu.umn.msi.tropix.jobs.activities.descriptions.ActivityDescriptions;
import edu.umn.msi.tropix.jobs.activities.descriptions.CreateTropixFileDescription;
import edu.umn.msi.tropix.jobs.activities.descriptions.JobDescription;
import edu.umn.msi.tropix.jobs.activities.descriptions.PollJobDescription;
import edu.umn.msi.tropix.jobs.activities.descriptions.SubmitGalaxyDescription;
import edu.umn.msi.tropix.models.GalaxyTool;
import edu.umn.msi.tropix.webgui.client.AsyncCallbackImpl;
import edu.umn.msi.tropix.webgui.client.Resources;
import edu.umn.msi.tropix.webgui.client.components.ComponentFactory;
import edu.umn.msi.tropix.webgui.client.components.MetadataInputComponentFactory;
import edu.umn.msi.tropix.webgui.client.components.WindowComponent;
import edu.umn.msi.tropix.webgui.client.components.impl.WindowComponentImpl;
import edu.umn.msi.tropix.webgui.client.components.tree.LocationFactory;
import edu.umn.msi.tropix.webgui.client.components.tree.TreeComponent;
import edu.umn.msi.tropix.webgui.client.components.tree.TreeComponentFactory;
import edu.umn.msi.tropix.webgui.client.components.tree.TreeItem;
import edu.umn.msi.tropix.webgui.client.components.tree.TreeItemPredicates;
import edu.umn.msi.tropix.webgui.client.components.tree.TreeOptions;
import edu.umn.msi.tropix.webgui.client.utils.Listener;
import edu.umn.msi.tropix.webgui.client.utils.ListenerList;
import edu.umn.msi.tropix.webgui.client.utils.ListenerLists;
import edu.umn.msi.tropix.webgui.client.utils.Lists;
import edu.umn.msi.tropix.webgui.client.utils.Maps;
import edu.umn.msi.tropix.webgui.client.utils.Sets;
import edu.umn.msi.tropix.webgui.client.utils.StringUtils;
import edu.umn.msi.tropix.webgui.client.widgets.Form;
import edu.umn.msi.tropix.webgui.client.widgets.HasValidation;
import edu.umn.msi.tropix.webgui.client.widgets.ItemWrapper;
import edu.umn.msi.tropix.webgui.client.widgets.PopOutWindowBuilder;
import edu.umn.msi.tropix.webgui.client.widgets.SmartUtils;
import edu.umn.msi.tropix.webgui.services.galaxy.GalaxyService;
import edu.umn.msi.tropix.webgui.services.jobs.JobSubmitService;

public class GalaxyActionComponentFactoryImpl implements ComponentFactory<GalaxyActionEvent, WindowComponent<Window>> {
  private final TreeComponentFactory treeComponentFactory;
  private final LocationFactory locationFactory;

  @Inject
  public GalaxyActionComponentFactoryImpl(final MetadataInputComponentFactory metadataInputComponentFactory,
                                          final TreeComponentFactory treeComponentFactory, 
                                          final LocationFactory locationFactory) {
    this.treeComponentFactory = treeComponentFactory;
    this.locationFactory = locationFactory;
  }

  private interface Validator {
    boolean isValid();
  }
  
  class GalaxyActionComponentImpl extends WindowComponentImpl<Window> implements HasValidation, ChangedHandler {
    private final ListenerList<Boolean> validationListeners = ListenerLists.getInstance();
    private final GalaxyTool galaxyTool;
    private final VLayout layout = new VLayout();
    private final List<TextItem> outputNameItems = Lists.newArrayList();
    private final List<Validator> validators = Lists.newArrayList(); 
    private TreeComponent parentTreeComponent;

    private Tool tool;

    private final AsyncCallbackImpl<Tool> callback = new AsyncCallbackImpl<Tool>() {
      @Override
      protected void handleSuccess() {
        tool = getResult();
        init();
      }
    };
    
    final Input getInput(final Param param, final FormItem formItem) {
      final Input input = new Input();
      input.setName(param.getName());
      formItem.addChangedHandler(new ChangedHandler() {
        public void onChanged(final ChangedEvent event) {
          input.setValue(StringUtils.toString(formItem.getValue().toString()));
        }              
      });
      formItem.addChangedHandler(this);
      if(StringUtils.hasText(param.getValue())) {
        String defaultValue = param.getValue().toString();
        formItem.setValue(defaultValue);
        input.setValue(defaultValue);
      }
      return input;
    }
    
    final Canvas buildInputCanvas(final List<Input> inputs, final List<InputType> inputDefinitions) {
      final VLayout layout = new VLayout();
      layout.setMembersMargin(4);
      for(InputType inputDefinition : inputDefinitions) {
        if(inputDefinition instanceof Param) {
          final Param param = (Param) inputDefinition;
          final String value = param.getValue();
          final ParamType paramType = param.getType();
          if(paramType == ParamType.TEXT || paramType == ParamType.INTEGER || paramType == ParamType.FLOAT) {
            final TextItem textItem = new TextItem(param.getName(), param.getLabel());
            inputs.add(getInput(param, textItem));
            textItem.setValue(StringUtils.toString(param.getValue()));
            validators.add(new Validator() {
              public boolean isValid() {
                boolean isValid = true;
                final String value = StringUtils.toString(textItem.getValue());
                if(paramType == ParamType.INTEGER || paramType == ParamType.FLOAT) {
                  try {
                    if(paramType == ParamType.INTEGER) {
                      Integer.parseInt(value);
                    } else if(paramType == ParamType.FLOAT) {
                      Float.parseFloat(value);
                    }
                  } catch(NumberFormatException nfe) {
                    isValid = false;
                  }
                }
                return isValid;
              }              
            });
            layout.addMember(new ItemWrapper(textItem));
          } else if(paramType == ParamType.SELECT) {
            final SelectItem selectItem = new SelectItem(param.getName(), param.getLabel());
            inputs.add(getInput(param, selectItem));
            final LinkedHashMap<String, String> optionMap = Maps.newLinkedHashMap();
            for(ParamOption option : param.getOption()) {
              optionMap.put(option.getValueAttribute(), option.getValue());
            }
            selectItem.setValueMap(optionMap);
            validators.add(new Validator() {
              public boolean isValid() {
                final String value = StringUtils.toString(selectItem.getValue());
                return optionMap.containsKey(value);
              }              
            });
            layout.addMember(new ItemWrapper(selectItem));
          } else if(paramType == ParamType.BOOLEAN) {
            final CheckboxItem checkItem = new CheckboxItem(param.getName(), param.getLabel());
            final Input input = new Input();
            input.setName(param.getName());
            checkItem.addChangedHandler(new ChangedHandler() {
              public void onChanged(final ChangedEvent event) {
                final Boolean checked = (Boolean) checkItem.getValue();
                input.setValue(StringUtils.toString(checked ? param.getTruevalue() : param.getFalsevalue()));
              }              
            });
            if(value != null && value.equals(param.getTruevalue())) {
              checkItem.setValue(true);
              input.setValue(param.getTruevalue());
            } else {
              checkItem.setValue(false);
              input.setValue(param.getFalsevalue());
            }
            inputs.add(input);
            layout.addMember(new ItemWrapper(checkItem));
          } else if(paramType == ParamType.DATA) {
            final TreeOptions treeOptions = new TreeOptions();
            treeOptions.setInitialItems(locationFactory.getTropixObjectSourceRootItems(null));
            System.out.println("Format: " + param.getFormat());
            final String extension = GalaxyFormatUtils.formatToExtension(param.getFormat());
            treeOptions.setShowPredicate(TreeItemPredicates.getTropixFileTreeItemPredicate(extension, true));
            treeOptions.setSelectionPredicate(TreeItemPredicates.getTropixFileTreeItemPredicate(extension, false));
            final TreeComponent treeComponent = treeComponentFactory.get(treeOptions);
            final Input input = new Input();
            input.setName(param.getName());
            treeComponent.addSelectionListener(new Listener<TreeItem>() {
              public void onEvent(final TreeItem location) {
                input.setValue(location.getId());
                onUpdate();
              }              
            });
            validators.add(new Validator() {
              public boolean isValid() {
                return treeComponent.getSelection() != null;
              }              
            });
            inputs.add(input);
            final TreeGrid treeGrid = treeComponent.get();
            treeGrid.setWidth("100%");
            treeGrid.setHeight("150px");
            treeGrid.setMargin(10);

            final Label label = SmartUtils.smartParagraph(param.getLabel() + ":");
            layout.addMember(label);
            layout.addMember(treeGrid);
            
          }
        }
      }
      onUpdate();
      return layout;
    }
    
    private void init() {
      final TreeOptions treeOptions = new TreeOptions();
      treeOptions.setInitialItems(locationFactory.getTropixObjectDestinationRootItems(null));
      treeOptions.setShowPredicate(TreeItemPredicates.getDestinationsPredicate(true));
      treeOptions.setSelectionPredicate(TreeItemPredicates.getDestinationsPredicate(false));
      // TODO: Handle initial items...

      parentTreeComponent = treeComponentFactory.get(treeOptions);
      final TreeGrid treeGrid = parentTreeComponent.get();
      treeGrid.setWidth("100%");
      treeGrid.setHeight("150px");
      parentTreeComponent.addSelectionListener(new Listener<TreeItem>() {
        public void onEvent(final TreeItem event) {
          onUpdate();
        }
      });
      validators.add(new Validator() {
        public boolean isValid() {
          return parentTreeComponent.getSelection() != null;
        }        
      });
      final Label outputLocationLabel = SmartUtils.smartParagraph("Select location for output files");

      final Label outputNameLabel = SmartUtils.smartParagraph("Specify names for output files");
      final Form outputForm = new Form();
      final List<Data> outputs = tool.getOutputs().getData();
      for(final Data data : outputs) {
        final TextItem textItem = new TextItem(data.getName(), data.getLabel());
        textItem.addChangedHandler(this);
        outputNameItems.add(textItem);
        validators.add(new Validator() {
          public boolean isValid() {
            return StringUtils.hasText(textItem.getValue());
          }
        });
      }
      outputForm.setItems(outputNameItems.toArray(new TextItem[outputNameItems.size()]));
      
      final VLayout outputLayout = new VLayout();
      outputLayout.setGroupTitle("Outputs");
      outputLayout.setIsGroup(true);
      outputLayout.setWidth("95%");
      outputLayout.setLayoutAlign(Alignment.CENTER);
      outputLayout.setPadding(10);
      outputLayout.setMembersMargin(2);

      outputLayout.addMember(outputLocationLabel);
      outputLayout.addMember(treeGrid);
      outputLayout.addMember(outputNameLabel);
      outputLayout.addMember(outputForm);
      
      layout.addMember(outputLayout);

      final RootInput rootInput = new RootInput();
      final VLayout inputsLayout = new VLayout();
      inputsLayout.addMember(buildInputCanvas(rootInput.getInput(), tool.getInputs().getInputElement()));
      inputsLayout.setGroupTitle("Inputs");
      inputsLayout.setIsGroup(true);
      inputsLayout.setPadding(10);
      inputsLayout.setMembersMargin(2);
      inputsLayout.setLayoutAlign(Alignment.CENTER);
      inputsLayout.setWidth("95%");

      layout.addMember(inputsLayout);
      final Button submitButton = SmartUtils.getButton("Submit Job", Resources.GO, new Command() {
        public void execute() {
          try {
            final JobDescription jobDescription = new JobDescription(galaxyTool.getName());
            final SubmitGalaxyDescription galaxyJobDescription = new SubmitGalaxyDescription();
            galaxyJobDescription.setJobDescription(jobDescription);
            // TODO: Display server list and fetch this value...
            galaxyJobDescription.setServiceUrl("local://Galaxy");
            galaxyJobDescription.setGalaxyToolId(galaxyTool.getId());
            galaxyJobDescription.setInput(rootInput);
            final PollJobDescription pollJobDescription = ActivityDescriptions.buildPollDescription(galaxyJobDescription);
            final Set<ActivityDescription> descriptions = Sets.<ActivityDescription>newHashSet(galaxyJobDescription, pollJobDescription);
            final Outputs outputs = tool.getOutputs();
            if(outputs != null) {
              int index = 0;
              for(Data data : outputs.getData()) {
                final CreateTropixFileDescription createResultDescription = ActivityDescriptions.buildCreateResultFile(pollJobDescription, index);
                createResultDescription.setName(outputNameItems.get(index).getValue().toString() + "." + data.getFormat());
                createResultDescription.setDestinationId(parentTreeComponent.getSelection().getId());
                createResultDescription.setCommitted(true);
                final String format = data.getFormat();
                if(StringUtils.hasText(format)) {
                  final String extension = GalaxyFormatUtils.formatToExtension(format);
                  createResultDescription.setExtension(extension);
                }
                descriptions.add(createResultDescription);
                index++;
              }
            }
            JobSubmitService.Util.getInstance().submit(descriptions, new AsyncCallbackImpl<Void>());
            destroy();
          } catch(RuntimeException e) { 
            e.printStackTrace();
            throw e;
          }
        }
      });
      layout.addMember(submitButton);
      SmartUtils.enabledWhenValid(submitButton, this);
    }
    
    private void onUpdate() {
      final boolean isValid = isValid();
      System.out.println("Is valid " + isValid);
      validationListeners.onEvent(isValid);
    }
    
    GalaxyActionComponentImpl(final GalaxyActionEvent event) {
      galaxyTool = event.getGalaxyTool();
      layout.setOverflow(Overflow.AUTO);      
      //layout.setWidth(600);
      //layout.setHeight(500);
      layout.setPadding(10);
      layout.setMembersMargin(10);
      final Window window = PopOutWindowBuilder.titled(galaxyTool.getName()).withContents(layout).get();
      setWidget(window);
      GalaxyService.Util.getInstance().loadTool(galaxyTool.getId(), callback);
    }

    public void addValidationListener(final Listener<Boolean> validationListener) {
      validationListeners.add(validationListener);
    }

    public boolean isValid() {
      boolean isValid = true;
      for(Validator validator : validators) {
        if(!validator.isValid()) {
          isValid = false;
          break;
        }
      }
      return isValid;
    }

    public void onChanged(final ChangedEvent event) {
      onUpdate();
    }
    
  }

  public WindowComponent<Window> get(final GalaxyActionEvent input) {
    return new GalaxyActionComponentImpl(input);
  }
  
}
