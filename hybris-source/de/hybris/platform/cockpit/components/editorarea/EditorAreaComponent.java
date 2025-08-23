package de.hybris.platform.cockpit.components.editorarea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Div;

public class EditorAreaComponent extends Div
{
    private static final Logger LOG = LoggerFactory.getLogger(EditorAreaComponent.class);


    public EditorAreaComponent()
    {
        // Byte code:
        //   0: aload_0
        //   1: invokespecial <init> : ()V
        //   4: aload_0
        //   5: ldc '100%'
        //   7: invokevirtual setWidth : (Ljava/lang/String;)V
        //   10: aload_0
        //   11: ldc '100%'
        //   13: invokevirtual setHeight : (Ljava/lang/String;)V
        //   16: aload_0
        //   17: ldc 'right'
        //   19: invokevirtual setAlign : (Ljava/lang/String;)V
        //   22: aload_0
        //   23: ldc 'onmouseup: comm.sendClick(#{self},null)'
        //   25: invokevirtual setAction : (Ljava/lang/String;)V
        //   28: aload_0
        //   29: ldc 'onClick'
        //   31: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$1
        //   34: dup
        //   35: aload_0
        //   36: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;)V
        //   39: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   42: pop
        //   43: invokestatic getCurrentSession : ()Lde/hybris/platform/cockpit/session/UISession;
        //   46: invokeinterface getCurrentPerspective : ()Lde/hybris/platform/cockpit/session/UICockpitPerspective;
        //   51: astore_1
        //   52: new de/hybris/platform/cockpit/components/sectionpanel/SectionPanel
        //   55: dup
        //   56: invokespecial <init> : ()V
        //   59: astore_2
        //   60: aload_2
        //   61: aload_0
        //   62: invokevirtual setParent : (Lorg/zkoss/zk/ui/Component;)V
        //   65: aload_2
        //   66: ldc '100%'
        //   68: invokevirtual setWidth : (Ljava/lang/String;)V
        //   71: aload_2
        //   72: ldc '100%'
        //   74: invokevirtual setHeight : (Ljava/lang/String;)V
        //   77: aload_2
        //   78: iconst_1
        //   79: invokevirtual setFlatSectionLayout : (Z)V
        //   82: aload_2
        //   83: ldc '11em'
        //   85: invokevirtual setRowLabelWidth : (Ljava/lang/String;)V
        //   88: aload_2
        //   89: iconst_1
        //   90: invokevirtual setLazyLoad : (Z)V
        //   93: aload_2
        //   94: aload_1
        //   95: invokeinterface getEditorArea : ()Lde/hybris/platform/cockpit/session/UIEditorArea;
        //   100: invokeinterface getEditorAreaController : ()Lde/hybris/platform/cockpit/session/EditorAreaController;
        //   105: invokeinterface getSectionPanelModel : ()Lde/hybris/platform/cockpit/components/sectionpanel/SectionPanelModel;
        //   110: invokevirtual setModel : (Lde/hybris/platform/cockpit/components/sectionpanel/SectionPanelModel;)V
        //   113: aload_2
        //   114: aload_1
        //   115: invokeinterface getEditorArea : ()Lde/hybris/platform/cockpit/session/UIEditorArea;
        //   120: invokeinterface getEditorAreaController : ()Lde/hybris/platform/cockpit/session/EditorAreaController;
        //   125: invokeinterface getSectionRenderer : ()Lde/hybris/platform/cockpit/components/sectionpanel/SectionRenderer;
        //   130: invokevirtual setSectionRenderer : (Lde/hybris/platform/cockpit/components/sectionpanel/SectionRenderer;)V
        //   133: aload_2
        //   134: aload_1
        //   135: invokeinterface getEditorArea : ()Lde/hybris/platform/cockpit/session/UIEditorArea;
        //   140: invokeinterface getEditorAreaController : ()Lde/hybris/platform/cockpit/session/EditorAreaController;
        //   145: invokeinterface getSectionRowRenderer : ()Lde/hybris/platform/cockpit/components/sectionpanel/SectionRowRenderer;
        //   150: invokevirtual setSectionRowRenderer : (Lde/hybris/platform/cockpit/components/sectionpanel/SectionRowRenderer;)V
        //   153: aload_2
        //   154: aload_1
        //   155: invokeinterface getEditorArea : ()Lde/hybris/platform/cockpit/session/UIEditorArea;
        //   160: invokeinterface getEditorAreaController : ()Lde/hybris/platform/cockpit/session/EditorAreaController;
        //   165: invokeinterface getSectionPanelLabelRenderer : ()Lde/hybris/platform/cockpit/components/sectionpanel/SectionPanelLabelRenderer;
        //   170: invokevirtual setSectionPanelLabelRenderer : (Lde/hybris/platform/cockpit/components/sectionpanel/SectionPanelLabelRenderer;)V
        //   173: aload_2
        //   174: invokestatic getCurrentSession : ()Lde/hybris/platform/cockpit/session/UISession;
        //   177: invokeinterface getCurrentPerspective : ()Lde/hybris/platform/cockpit/session/UICockpitPerspective;
        //   182: invokeinterface getEditorArea : ()Lde/hybris/platform/cockpit/session/UIEditorArea;
        //   187: invokeinterface getEditorAreaController : ()Lde/hybris/platform/cockpit/session/EditorAreaController;
        //   192: instanceof de/hybris/platform/cockpit/session/MutableSectionModelController
        //   195: ifeq -> 228
        //   198: invokestatic getCurrentSession : ()Lde/hybris/platform/cockpit/session/UISession;
        //   201: invokeinterface getUiAccessRightService : ()Lde/hybris/platform/cockpit/services/security/UIAccessRightService;
        //   206: invokestatic getCurrentSession : ()Lde/hybris/platform/cockpit/session/UISession;
        //   209: invokeinterface getUser : ()Lde/hybris/platform/core/model/user/UserModel;
        //   214: ldc 'cockpit.personalizedconfiguration'
        //   216: invokeinterface canWrite : (Lde/hybris/platform/core/model/user/UserModel;Ljava/lang/String;)Z
        //   221: ifeq -> 228
        //   224: iconst_1
        //   225: goto -> 229
        //   228: iconst_0
        //   229: invokevirtual setSectionsChangeAllowed : (Z)V
        //   232: aload_2
        //   233: ldc 'onAllSectionsShow'
        //   235: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$2
        //   238: dup
        //   239: aload_0
        //   240: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;)V
        //   243: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   246: pop
        //   247: aload_2
        //   248: ldc 'onAllRowsShow'
        //   250: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$3
        //   253: dup
        //   254: aload_0
        //   255: aload_2
        //   256: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;Lde/hybris/platform/cockpit/components/sectionpanel/SectionPanel;)V
        //   259: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   262: pop
        //   263: aload_2
        //   264: ldc 'onRowShow'
        //   266: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$4
        //   269: dup
        //   270: aload_0
        //   271: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;)V
        //   274: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   277: pop
        //   278: aload_2
        //   279: ldc 'onRowHide'
        //   281: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$5
        //   284: dup
        //   285: aload_0
        //   286: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;)V
        //   289: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   292: pop
        //   293: aload_2
        //   294: ldc 'onRowMoved'
        //   296: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$6
        //   299: dup
        //   300: aload_0
        //   301: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;)V
        //   304: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   307: pop
        //   308: aload_2
        //   309: ldc 'onSectionHide'
        //   311: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$7
        //   314: dup
        //   315: aload_0
        //   316: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;)V
        //   319: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   322: pop
        //   323: aload_2
        //   324: ldc 'onSectionShow'
        //   326: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$8
        //   329: dup
        //   330: aload_0
        //   331: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;)V
        //   334: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   337: pop
        //   338: aload_2
        //   339: ldc 'onMessageClicked'
        //   341: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$9
        //   344: dup
        //   345: aload_0
        //   346: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;)V
        //   349: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   352: pop
        //   353: aload_2
        //   354: ldc 'onLater'
        //   356: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$10
        //   359: dup
        //   360: aload_0
        //   361: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;)V
        //   364: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   367: pop
        //   368: aload_2
        //   369: ldc 'onSectionRemoved'
        //   371: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$11
        //   374: dup
        //   375: aload_0
        //   376: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;)V
        //   379: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   382: pop
        //   383: aload_2
        //   384: ldc 'onSectionAdded'
        //   386: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$12
        //   389: dup
        //   390: aload_0
        //   391: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;)V
        //   394: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   397: pop
        //   398: aload_2
        //   399: ldc 'onSectionLabelChange'
        //   401: new de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent$13
        //   404: dup
        //   405: aload_0
        //   406: invokespecial <init> : (Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;)V
        //   409: invokevirtual addEventListener : (Ljava/lang/String;Lorg/zkoss/zk/ui/event/EventListener;)Z
        //   412: pop
        //   413: aload_2
        //   414: invokevirtual afterCompose : ()V
        //   417: aload_1
        //   418: invokeinterface getEditorArea : ()Lde/hybris/platform/cockpit/session/UIEditorArea;
        //   423: invokeinterface getEditorAreaController : ()Lde/hybris/platform/cockpit/session/EditorAreaController;
        //   428: instanceof de/hybris/platform/cockpit/session/impl/AbstractEditorAreaController
        //   431: ifeq -> 466
        //   434: aload_1
        //   435: invokeinterface getEditorArea : ()Lde/hybris/platform/cockpit/session/UIEditorArea;
        //   440: invokeinterface getEditorAreaController : ()Lde/hybris/platform/cockpit/session/EditorAreaController;
        //   445: checkcast de/hybris/platform/cockpit/session/impl/AbstractEditorAreaController
        //   448: aload_2
        //   449: invokevirtual doAfterCompose : (Lorg/zkoss/zk/ui/Component;)V
        //   452: goto -> 466
        //   455: astore_3
        //   456: getstatic de/hybris/platform/cockpit/components/editorarea/EditorAreaComponent.LOG : Lorg/slf4j/Logger;
        //   459: ldc 'Could not apply contoller correctly.'
        //   461: invokeinterface warn : (Ljava/lang/String;)V
        //   466: return
        // Line number table:
        //   Java source line number -> byte code offset
        //   #34	-> 0
        //   #36	-> 4
        //   #37	-> 10
        //   #38	-> 16
        //   #39	-> 22
        //   #41	-> 28
        //   #50	-> 43
        //   #52	-> 52
        //   #53	-> 60
        //   #54	-> 65
        //   #55	-> 71
        //   #56	-> 77
        //   #57	-> 82
        //   #58	-> 88
        //   #59	-> 93
        //   #60	-> 113
        //   #61	-> 133
        //   #62	-> 153
        //   #63	-> 165
        //   #62	-> 170
        //   #64	-> 173
        //   #65	-> 187
        //   #67	-> 198
        //   #68	-> 201
        //   #69	-> 206
        //   #64	-> 229
        //   #72	-> 232
        //   #81	-> 247
        //   #98	-> 263
        //   #107	-> 278
        //   #116	-> 293
        //   #125	-> 308
        //   #134	-> 323
        //   #143	-> 338
        //   #152	-> 353
        //   #162	-> 368
        //   #176	-> 383
        //   #190	-> 398
        //   #204	-> 413
        //   #205	-> 417
        //   #209	-> 434
        //   #214	-> 452
        //   #211	-> 455
        //   #213	-> 456
        //   #216	-> 466
        // Local variable table:
        //   start	length	slot	name	descriptor
        //   456	10	3	e	Ljava/lang/Exception;
        //   0	467	0	this	Lde/hybris/platform/cockpit/components/editorarea/EditorAreaComponent;
        //   52	415	1	perspective	Lde/hybris/platform/cockpit/session/UICockpitPerspective;
        //   60	407	2	sectionpanel	Lde/hybris/platform/cockpit/components/sectionpanel/SectionPanel;
        // Exception table:
        //   from	to	target	type
        //   434	452	455	java/lang/Exception
    }
}
