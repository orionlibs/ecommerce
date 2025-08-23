package de.hybris.platform.util;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.apache.log4j.Logger;

public final class SerializableChecker extends ObjectOutputStream
{
    private static final Logger LOG = Logger.getLogger(SerializableChecker.class);
    private static final NoopOutputStream DUMMY_OUTPUT_STREAM = new NoopOutputStream();
    private static boolean available = true;
    private static final Method LOOKUP_METHOD;
    private static final Method GET_CLASS_DATA_LAYOUT_METHOD;
    private static final Method GET_NUM_OBJ_FIELDS_METHOD;
    private static final Method GET_OBJ_FIELD_VALUES_METHOD;
    private static final Method GET_FIELD_METHOD;
    private static final Method HAS_WRITE_REPLACE_METHOD_METHOD;
    private static final Method INVOKE_WRITE_REPLACE_METHOD;
    private final LinkedList<TraceSlot> traceStack = new LinkedList<>();
    private final Map checked = new IdentityHashMap<>();
    private final LinkedList<String> nameStack = new LinkedList<>();
    private Object root;
    private final Map writeObjectMethodCache = new HashMap<>();
    private String simpleName = "";
    private String fieldDescription;
    private final NotSerializableException exception;

    static
    {
        try
        {
            LOOKUP_METHOD = ObjectStreamClass.class.getDeclaredMethod("lookup", new Class[] {Class.class, boolean.class});
            LOOKUP_METHOD.setAccessible(true);
            GET_CLASS_DATA_LAYOUT_METHOD = ObjectStreamClass.class.getDeclaredMethod("getClassDataLayout", (Class[])null);
            GET_CLASS_DATA_LAYOUT_METHOD.setAccessible(true);
            GET_NUM_OBJ_FIELDS_METHOD = ObjectStreamClass.class.getDeclaredMethod("getNumObjFields", (Class[])null);
            GET_NUM_OBJ_FIELDS_METHOD.setAccessible(true);
            GET_OBJ_FIELD_VALUES_METHOD = ObjectStreamClass.class.getDeclaredMethod("getObjFieldValues", new Class[] {Object.class, Object[].class});
            GET_OBJ_FIELD_VALUES_METHOD.setAccessible(true);
            GET_FIELD_METHOD = ObjectStreamField.class.getDeclaredMethod("getField", (Class[])null);
            GET_FIELD_METHOD.setAccessible(true);
            HAS_WRITE_REPLACE_METHOD_METHOD = ObjectStreamClass.class.getDeclaredMethod("hasWriteReplaceMethod", (Class[])null);
            HAS_WRITE_REPLACE_METHOD_METHOD.setAccessible(true);
            INVOKE_WRITE_REPLACE_METHOD = ObjectStreamClass.class.getDeclaredMethod("invokeWriteReplace", new Class[] {Object.class});
            INVOKE_WRITE_REPLACE_METHOD.setAccessible(true);
        }
        catch(SecurityException e)
        {
            available = false;
            throw new RuntimeException(e);
        }
        catch(NoSuchMethodException e)
        {
            available = false;
            throw new RuntimeException(e);
        }
    }

    public SerializableChecker(NotSerializableException exception) throws IOException
    {
        this.exception = exception;
    }


    public void reset() throws IOException
    {
        this.root = null;
        this.checked.clear();
        this.fieldDescription = null;
        this.simpleName = null;
        this.traceStack.clear();
        this.nameStack.clear();
        this.writeObjectMethodCache.clear();
    }


    public void check(Object obj)
    {
        // Byte code:
        //   0: getstatic de/hybris/platform/util/SerializableChecker.LOG : Lorg/apache/log4j/Logger;
        //   3: aload_1
        //   4: invokevirtual getClass : ()Ljava/lang/Class;
        //   7: aload_1
        //   8: <illegal opcode> makeConcatWithConstants : (Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/String;
        //   13: invokevirtual info : (Ljava/lang/Object;)V
        //   16: aload_1
        //   17: invokevirtual getClass : ()Ljava/lang/Class;
        //   20: astore_2
        //   21: aload_0
        //   22: getfield nameStack : Ljava/util/LinkedList;
        //   25: aload_0
        //   26: getfield simpleName : Ljava/lang/String;
        //   29: invokevirtual add : (Ljava/lang/Object;)Z
        //   32: pop
        //   33: aload_0
        //   34: getfield traceStack : Ljava/util/LinkedList;
        //   37: new de/hybris/platform/util/SerializableChecker$TraceSlot
        //   40: dup
        //   41: aload_1
        //   42: aload_0
        //   43: getfield fieldDescription : Ljava/lang/String;
        //   46: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/String;)V
        //   49: invokevirtual add : (Ljava/lang/Object;)Z
        //   52: pop
        //   53: aload_1
        //   54: instanceof java/io/Serializable
        //   57: ifne -> 93
        //   60: aload_2
        //   61: invokestatic isProxyClass : (Ljava/lang/Class;)Z
        //   64: ifne -> 93
        //   67: new de/hybris/platform/util/SerializableChecker$JaloNotSerializableException
        //   70: dup
        //   71: aload_0
        //   72: aload_1
        //   73: invokevirtual getClass : ()Ljava/lang/Class;
        //   76: invokevirtual getName : ()Ljava/lang/String;
        //   79: invokevirtual toPrettyPrintedStack : (Ljava/lang/String;)Ljava/lang/String;
        //   82: invokevirtual toString : ()Ljava/lang/String;
        //   85: aload_0
        //   86: getfield exception : Ljava/io/NotSerializableException;
        //   89: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
        //   92: athrow
        //   93: getstatic de/hybris/platform/util/SerializableChecker.LOOKUP_METHOD : Ljava/lang/reflect/Method;
        //   96: aconst_null
        //   97: iconst_2
        //   98: anewarray java/lang/Object
        //   101: dup
        //   102: iconst_0
        //   103: aload_2
        //   104: aastore
        //   105: dup
        //   106: iconst_1
        //   107: getstatic java/lang/Boolean.TRUE : Ljava/lang/Boolean;
        //   110: aastore
        //   111: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //   114: checkcast java/io/ObjectStreamClass
        //   117: astore_3
        //   118: getstatic de/hybris/platform/util/SerializableChecker.HAS_WRITE_REPLACE_METHOD_METHOD : Ljava/lang/reflect/Method;
        //   121: aload_3
        //   122: aconst_null
        //   123: checkcast [Ljava/lang/Object;
        //   126: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //   129: checkcast java/lang/Boolean
        //   132: invokevirtual booleanValue : ()Z
        //   135: ifeq -> 169
        //   138: getstatic de/hybris/platform/util/SerializableChecker.INVOKE_WRITE_REPLACE_METHOD : Ljava/lang/reflect/Method;
        //   141: aload_3
        //   142: iconst_1
        //   143: anewarray java/lang/Object
        //   146: dup
        //   147: iconst_0
        //   148: aload_1
        //   149: aastore
        //   150: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //   153: dup
        //   154: astore_1
        //   155: ifnull -> 169
        //   158: aload_1
        //   159: invokevirtual getClass : ()Ljava/lang/Class;
        //   162: dup
        //   163: astore #4
        //   165: aload_2
        //   166: if_acmpne -> 172
        //   169: goto -> 202
        //   172: aload #4
        //   174: astore_2
        //   175: goto -> 93
        //   178: astore #4
        //   180: new java/lang/RuntimeException
        //   183: dup
        //   184: aload #4
        //   186: invokespecial <init> : (Ljava/lang/Throwable;)V
        //   189: athrow
        //   190: astore #4
        //   192: new java/lang/RuntimeException
        //   195: dup
        //   196: aload #4
        //   198: invokespecial <init> : (Ljava/lang/Throwable;)V
        //   201: athrow
        //   202: aload_2
        //   203: invokevirtual isPrimitive : ()Z
        //   206: ifeq -> 224
        //   209: getstatic de/hybris/platform/util/SerializableChecker.LOG : Lorg/apache/log4j/Logger;
        //   212: aload_2
        //   213: <illegal opcode> makeConcatWithConstants : (Ljava/lang/Class;)Ljava/lang/String;
        //   218: invokevirtual info : (Ljava/lang/Object;)V
        //   221: goto -> 800
        //   224: aload_2
        //   225: invokevirtual isArray : ()Z
        //   228: ifeq -> 334
        //   231: getstatic de/hybris/platform/util/SerializableChecker.LOG : Lorg/apache/log4j/Logger;
        //   234: aload_2
        //   235: <illegal opcode> makeConcatWithConstants : (Ljava/lang/Class;)Ljava/lang/String;
        //   240: invokevirtual info : (Ljava/lang/Object;)V
        //   243: aload_0
        //   244: getfield checked : Ljava/util/Map;
        //   247: aload_1
        //   248: aconst_null
        //   249: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   254: pop
        //   255: aload_2
        //   256: invokevirtual getComponentType : ()Ljava/lang/Class;
        //   259: astore #4
        //   261: aload #4
        //   263: invokevirtual isPrimitive : ()Z
        //   266: ifne -> 331
        //   269: aload_1
        //   270: checkcast [Ljava/lang/Object;
        //   273: astore #5
        //   275: iconst_0
        //   276: istore #6
        //   278: iload #6
        //   280: aload #5
        //   282: arraylength
        //   283: if_icmpge -> 331
        //   286: iload #6
        //   288: <illegal opcode> makeConcatWithConstants : (I)Ljava/lang/String;
        //   293: astore #7
        //   295: aload_0
        //   296: aload #7
        //   298: putfield simpleName : Ljava/lang/String;
        //   301: aload_0
        //   302: dup
        //   303: getfield fieldDescription : Ljava/lang/String;
        //   306: aload #7
        //   308: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   313: putfield fieldDescription : Ljava/lang/String;
        //   316: aload_0
        //   317: aload #5
        //   319: iload #6
        //   321: aaload
        //   322: invokevirtual check : (Ljava/lang/Object;)V
        //   325: iinc #6, 1
        //   328: goto -> 278
        //   331: goto -> 800
        //   334: aload_1
        //   335: instanceof java/io/Externalizable
        //   338: ifeq -> 411
        //   341: aload_2
        //   342: invokestatic isProxyClass : (Ljava/lang/Class;)Z
        //   345: ifne -> 411
        //   348: aload_1
        //   349: checkcast java/io/Externalizable
        //   352: astore #4
        //   354: aload #4
        //   356: new de/hybris/platform/util/SerializableChecker$1
        //   359: dup
        //   360: aload_0
        //   361: invokespecial <init> : (Lde/hybris/platform/util/SerializableChecker;)V
        //   364: invokeinterface writeExternal : (Ljava/io/ObjectOutput;)V
        //   369: goto -> 408
        //   372: astore #5
        //   374: aload #5
        //   376: instanceof de/hybris/platform/util/SerializableChecker$JaloNotSerializableException
        //   379: ifeq -> 388
        //   382: aload #5
        //   384: checkcast de/hybris/platform/util/SerializableChecker$JaloNotSerializableException
        //   387: athrow
        //   388: getstatic de/hybris/platform/util/SerializableChecker.LOG : Lorg/apache/log4j/Logger;
        //   391: aload #5
        //   393: invokevirtual getMessage : ()Ljava/lang/String;
        //   396: aload_0
        //   397: invokevirtual currentPath : ()Ljava/lang/StringBuffer;
        //   400: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/StringBuffer;)Ljava/lang/String;
        //   405: invokevirtual warn : (Ljava/lang/Object;)V
        //   408: goto -> 800
        //   411: aconst_null
        //   412: astore #4
        //   414: aload_0
        //   415: getfield writeObjectMethodCache : Ljava/util/Map;
        //   418: aload_2
        //   419: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
        //   424: astore #5
        //   426: aload #5
        //   428: ifnull -> 449
        //   431: aload #5
        //   433: instanceof java/lang/reflect/Method
        //   436: ifeq -> 504
        //   439: aload #5
        //   441: checkcast java/lang/reflect/Method
        //   444: astore #4
        //   446: goto -> 504
        //   449: aload_2
        //   450: ldc 'writeObject'
        //   452: iconst_1
        //   453: anewarray java/lang/Class
        //   456: dup
        //   457: iconst_0
        //   458: ldc java/io/ObjectOutputStream
        //   460: aastore
        //   461: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //   464: astore #4
        //   466: goto -> 504
        //   469: astore #6
        //   471: aload_0
        //   472: getfield writeObjectMethodCache : Ljava/util/Map;
        //   475: aload_2
        //   476: getstatic java/lang/Boolean.FALSE : Ljava/lang/Boolean;
        //   479: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   484: pop
        //   485: goto -> 504
        //   488: astore #6
        //   490: aload_0
        //   491: getfield writeObjectMethodCache : Ljava/util/Map;
        //   494: aload_2
        //   495: getstatic java/lang/Boolean.FALSE : Ljava/lang/Boolean;
        //   498: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   503: pop
        //   504: aload_1
        //   505: astore #6
        //   507: aload #4
        //   509: ifnull -> 682
        //   512: aconst_null
        //   513: astore #7
        //   515: new de/hybris/platform/util/SerializableChecker$1InterceptingObjectOutputStream
        //   518: dup
        //   519: aload_0
        //   520: aload #6
        //   522: invokespecial <init> : (Lde/hybris/platform/util/SerializableChecker;Ljava/lang/Object;)V
        //   525: astore #7
        //   527: aload #7
        //   529: aload_1
        //   530: invokevirtual writeObject : (Ljava/lang/Object;)V
        //   533: aload #7
        //   535: ifnull -> 679
        //   538: aload #7
        //   540: invokevirtual close : ()V
        //   543: goto -> 679
        //   546: astore #8
        //   548: getstatic de/hybris/platform/util/SerializableChecker.LOG : Lorg/apache/log4j/Logger;
        //   551: aload #7
        //   553: aload #8
        //   555: invokevirtual getMessage : ()Ljava/lang/String;
        //   558: <illegal opcode> makeConcatWithConstants : (Lde/hybris/platform/util/SerializableChecker$1InterceptingObjectOutputStream;Ljava/lang/String;)Ljava/lang/String;
        //   563: invokevirtual warn : (Ljava/lang/Object;)V
        //   566: goto -> 679
        //   569: astore #8
        //   571: aload #8
        //   573: instanceof de/hybris/platform/util/SerializableChecker$JaloNotSerializableException
        //   576: ifeq -> 585
        //   579: aload #8
        //   581: checkcast de/hybris/platform/util/SerializableChecker$JaloNotSerializableException
        //   584: athrow
        //   585: getstatic de/hybris/platform/util/SerializableChecker.LOG : Lorg/apache/log4j/Logger;
        //   588: aload #8
        //   590: invokevirtual getMessage : ()Ljava/lang/String;
        //   593: aload_0
        //   594: invokevirtual currentPath : ()Ljava/lang/StringBuffer;
        //   597: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/StringBuffer;)Ljava/lang/String;
        //   602: invokevirtual warn : (Ljava/lang/Object;)V
        //   605: aload #7
        //   607: ifnull -> 679
        //   610: aload #7
        //   612: invokevirtual close : ()V
        //   615: goto -> 679
        //   618: astore #8
        //   620: getstatic de/hybris/platform/util/SerializableChecker.LOG : Lorg/apache/log4j/Logger;
        //   623: aload #7
        //   625: aload #8
        //   627: invokevirtual getMessage : ()Ljava/lang/String;
        //   630: <illegal opcode> makeConcatWithConstants : (Lde/hybris/platform/util/SerializableChecker$1InterceptingObjectOutputStream;Ljava/lang/String;)Ljava/lang/String;
        //   635: invokevirtual warn : (Ljava/lang/Object;)V
        //   638: goto -> 679
        //   641: astore #9
        //   643: aload #7
        //   645: ifnull -> 676
        //   648: aload #7
        //   650: invokevirtual close : ()V
        //   653: goto -> 676
        //   656: astore #10
        //   658: getstatic de/hybris/platform/util/SerializableChecker.LOG : Lorg/apache/log4j/Logger;
        //   661: aload #7
        //   663: aload #10
        //   665: invokevirtual getMessage : ()Ljava/lang/String;
        //   668: <illegal opcode> makeConcatWithConstants : (Lde/hybris/platform/util/SerializableChecker$1InterceptingObjectOutputStream;Ljava/lang/String;)Ljava/lang/String;
        //   673: invokevirtual warn : (Ljava/lang/Object;)V
        //   676: aload #9
        //   678: athrow
        //   679: goto -> 800
        //   682: getstatic de/hybris/platform/util/SerializableChecker.GET_CLASS_DATA_LAYOUT_METHOD : Ljava/lang/reflect/Method;
        //   685: aload_3
        //   686: aconst_null
        //   687: checkcast [Ljava/lang/Object;
        //   690: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //   693: checkcast [Ljava/lang/Object;
        //   696: astore #7
        //   698: goto -> 713
        //   701: astore #8
        //   703: new java/lang/RuntimeException
        //   706: dup
        //   707: aload #8
        //   709: invokespecial <init> : (Ljava/lang/Throwable;)V
        //   712: athrow
        //   713: iconst_0
        //   714: istore #8
        //   716: iload #8
        //   718: aload #7
        //   720: arraylength
        //   721: if_icmpge -> 800
        //   724: aload #7
        //   726: iload #8
        //   728: aaload
        //   729: invokevirtual getClass : ()Ljava/lang/Class;
        //   732: ldc 'desc'
        //   734: invokevirtual getDeclaredField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
        //   737: astore #10
        //   739: aload #10
        //   741: iconst_1
        //   742: invokevirtual setAccessible : (Z)V
        //   745: aload #10
        //   747: aload #7
        //   749: iload #8
        //   751: aaload
        //   752: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
        //   755: checkcast java/io/ObjectStreamClass
        //   758: astore #9
        //   760: goto -> 775
        //   763: astore #10
        //   765: new java/lang/RuntimeException
        //   768: dup
        //   769: aload #10
        //   771: invokespecial <init> : (Ljava/lang/Throwable;)V
        //   774: athrow
        //   775: aload_0
        //   776: getfield checked : Ljava/util/Map;
        //   779: aload_1
        //   780: aconst_null
        //   781: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   786: pop
        //   787: aload_0
        //   788: aload_1
        //   789: aload #9
        //   791: invokevirtual checkFields : (Ljava/lang/Object;Ljava/io/ObjectStreamClass;)V
        //   794: iinc #8, 1
        //   797: goto -> 716
        //   800: aload_0
        //   801: getfield traceStack : Ljava/util/LinkedList;
        //   804: invokevirtual removeLast : ()Ljava/lang/Object;
        //   807: pop
        //   808: aload_0
        //   809: getfield nameStack : Ljava/util/LinkedList;
        //   812: invokevirtual removeLast : ()Ljava/lang/Object;
        //   815: pop
        //   816: return
        // Line number table:
        //   Java source line number -> byte code offset
        //   #187	-> 0
        //   #189	-> 16
        //   #190	-> 21
        //   #191	-> 33
        //   #193	-> 53
        //   #195	-> 67
        //   #203	-> 93
        //   #206	-> 118
        //   #207	-> 150
        //   #208	-> 159
        //   #210	-> 169
        //   #212	-> 172
        //   #221	-> 175
        //   #214	-> 178
        //   #216	-> 180
        //   #218	-> 190
        //   #220	-> 192
        //   #224	-> 202
        //   #227	-> 209
        //   #229	-> 224
        //   #231	-> 231
        //   #233	-> 243
        //   #234	-> 255
        //   #235	-> 261
        //   #237	-> 269
        //   #238	-> 275
        //   #240	-> 286
        //   #241	-> 295
        //   #242	-> 301
        //   #243	-> 316
        //   #238	-> 325
        //   #246	-> 331
        //   #247	-> 334
        //   #249	-> 348
        //   #252	-> 354
        //   #281	-> 369
        //   #274	-> 372
        //   #276	-> 374
        //   #278	-> 382
        //   #280	-> 388
        //   #282	-> 408
        //   #285	-> 411
        //   #286	-> 414
        //   #287	-> 426
        //   #289	-> 431
        //   #291	-> 439
        //   #298	-> 449
        //   #310	-> 466
        //   #301	-> 469
        //   #304	-> 471
        //   #310	-> 485
        //   #306	-> 488
        //   #309	-> 490
        //   #313	-> 504
        //   #314	-> 507
        //   #350	-> 512
        //   #353	-> 515
        //   #354	-> 527
        //   #366	-> 533
        //   #370	-> 538
        //   #375	-> 543
        //   #372	-> 546
        //   #374	-> 548
        //   #375	-> 566
        //   #356	-> 569
        //   #358	-> 571
        //   #360	-> 579
        //   #362	-> 585
        //   #366	-> 605
        //   #370	-> 610
        //   #375	-> 615
        //   #372	-> 618
        //   #374	-> 620
        //   #375	-> 638
        //   #366	-> 641
        //   #370	-> 648
        //   #375	-> 653
        //   #372	-> 656
        //   #374	-> 658
        //   #377	-> 676
        //   #378	-> 679
        //   #384	-> 682
        //   #389	-> 698
        //   #386	-> 701
        //   #388	-> 703
        //   #390	-> 713
        //   #395	-> 724
        //   #396	-> 739
        //   #397	-> 745
        //   #402	-> 760
        //   #399	-> 763
        //   #401	-> 765
        //   #403	-> 775
        //   #404	-> 787
        //   #390	-> 794
        //   #408	-> 800
        //   #409	-> 808
        //   #410	-> 816
        // Local variable table:
        //   start	length	slot	name	descriptor
        //   165	4	4	repCl	Ljava/lang/Class;
        //   172	3	4	repCl	Ljava/lang/Class;
        //   118	60	3	desc	Ljava/io/ObjectStreamClass;
        //   180	10	4	e	Ljava/lang/IllegalAccessException;
        //   192	10	4	e	Ljava/lang/reflect/InvocationTargetException;
        //   295	30	7	arrayPos	Ljava/lang/String;
        //   278	53	6	i	I
        //   275	56	5	objs	[Ljava/lang/Object;
        //   261	70	4	ccl	Ljava/lang/Class;
        //   374	34	5	e	Ljava/lang/Exception;
        //   354	54	4	extObj	Ljava/io/Externalizable;
        //   471	14	6	e	Ljava/lang/SecurityException;
        //   490	14	6	e	Ljava/lang/NoSuchMethodException;
        //   548	18	8	e	Ljava/io/IOException;
        //   571	34	8	e	Ljava/lang/Exception;
        //   620	18	8	e	Ljava/io/IOException;
        //   658	18	10	e	Ljava/io/IOException;
        //   515	164	7	ioos	Lde/hybris/platform/util/SerializableChecker$1InterceptingObjectOutputStream;
        //   698	3	7	slots	[Ljava/lang/Object;
        //   703	10	8	e	Ljava/lang/Exception;
        //   739	21	10	descField	Ljava/lang/reflect/Field;
        //   760	3	9	slotDesc	Ljava/io/ObjectStreamClass;
        //   765	10	10	e	Ljava/lang/Exception;
        //   775	19	9	slotDesc	Ljava/io/ObjectStreamClass;
        //   716	84	8	i	I
        //   713	87	7	slots	[Ljava/lang/Object;
        //   414	386	4	writeObjectMethod	Ljava/lang/reflect/Method;
        //   426	374	5	o	Ljava/lang/Object;
        //   507	293	6	original	Ljava/lang/Object;
        //   0	817	0	this	Lde/hybris/platform/util/SerializableChecker;
        //   0	817	1	obj	Ljava/lang/Object;
        //   21	796	2	cls	Ljava/lang/Class;
        //   202	615	3	desc	Ljava/io/ObjectStreamClass;
        // Exception table:
        //   from	to	target	type
        //   93	169	178	java/lang/IllegalAccessException
        //   93	169	190	java/lang/reflect/InvocationTargetException
        //   172	175	178	java/lang/IllegalAccessException
        //   172	175	190	java/lang/reflect/InvocationTargetException
        //   354	369	372	java/lang/Exception
        //   449	466	469	java/lang/SecurityException
        //   449	466	488	java/lang/NoSuchMethodException
        //   515	533	569	java/lang/Exception
        //   515	533	641	finally
        //   538	543	546	java/io/IOException
        //   569	605	641	finally
        //   610	615	618	java/io/IOException
        //   641	643	641	finally
        //   648	653	656	java/io/IOException
        //   682	698	701	java/lang/Exception
        //   724	760	763	java/lang/Exception
    }


    private void checkFields(Object obj, ObjectStreamClass desc)
    {
        int numFields;
        LOG.info(">>>> checkFields(" + obj + ", " + desc + " )");
        try
        {
            numFields = ((Integer)GET_NUM_OBJ_FIELDS_METHOD.invoke(desc, (Object[])null)).intValue();
        }
        catch(IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch(InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
        if(numFields > 0)
        {
            ObjectStreamField[] fields = desc.getFields();
            Object[] objVals = new Object[numFields];
            int numPrimFields = fields.length - objVals.length;
            try
            {
                GET_OBJ_FIELD_VALUES_METHOD.invoke(desc, new Object[] {obj, objVals});
            }
            catch(IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch(InvocationTargetException e)
            {
                throw new RuntimeException(e);
            }
            for(int i = 0; i < objVals.length; i++)
            {
                if(!(objVals[i] instanceof String) && !(objVals[i] instanceof Number) && !(objVals[i] instanceof java.util.Date) && !(objVals[i] instanceof Boolean) && !(objVals[i] instanceof Class))
                {
                    if(!this.checked.containsKey(objVals[i]))
                    {
                        Field field;
                        ObjectStreamField fieldDesc = fields[numPrimFields + i];
                        try
                        {
                            field = (Field)GET_FIELD_METHOD.invoke(fieldDesc, (Object[])null);
                        }
                        catch(IllegalAccessException e)
                        {
                            throw new RuntimeException(e);
                        }
                        catch(InvocationTargetException e)
                        {
                            throw new RuntimeException(e);
                        }
                        this.simpleName = field.getName();
                        this.fieldDescription = field.toString();
                        LOG.info("simpleName: " + this.simpleName);
                        LOG.info("fieldDescription: " + this.fieldDescription);
                        check(objVals[i]);
                    }
                }
            }
        }
    }


    private StringBuffer currentPath()
    {
        StringBuffer b = new StringBuffer();
        for(Iterator<String> it = this.nameStack.iterator(); it.hasNext(); )
        {
            b.append(it.next());
            if(it.hasNext())
            {
                b.append('/');
            }
        }
        return b;
    }


    public static boolean isAvailable()
    {
        return available;
    }


    private String toPrettyPrintedStack(String type)
    {
        StringBuffer result = new StringBuffer(100);
        StringBuffer spaces = new StringBuffer();
        result.append("Unable to serialize class: ");
        result.append(type);
        result.append("\nField hierarchy is:");
        for(Iterator<TraceSlot> i = this.traceStack.listIterator(); i.hasNext(); )
        {
            spaces.append("  ");
            TraceSlot slot = i.next();
            result.append('\n').append(spaces).append(slot.fieldDescription);
            result.append(" [class=").append(slot.object.getClass().getName());
            LOG.info("slot.object: " + slot.object);
            result.append(']');
        }
        result.append(" <----- field that is not serializable");
        return result.toString();
    }


    protected void writeObjectOverride(Object obj) throws IOException
    {
        LOG.info("writeObjectOverride( " + obj + " )");
        if(!available)
        {
            return;
        }
        this.root = obj;
        if(this.fieldDescription == null)
        {
            LOG.info("root: " + this.root);
            this.fieldDescription = this.root.toString();
        }
        check(this.root);
    }
}
