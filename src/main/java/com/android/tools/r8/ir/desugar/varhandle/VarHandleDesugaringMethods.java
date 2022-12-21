// Copyright (c) 2022, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

// ***********************************************************************************
// GENERATED FILE. DO NOT EDIT! See GenerateVarHandleMethods.java.
// ***********************************************************************************

package com.android.tools.r8.ir.desugar.varhandle;

import com.android.tools.r8.cf.code.CfArithmeticBinop;
import com.android.tools.r8.cf.code.CfCheckCast;
import com.android.tools.r8.cf.code.CfConstClass;
import com.android.tools.r8.cf.code.CfConstNull;
import com.android.tools.r8.cf.code.CfConstNumber;
import com.android.tools.r8.cf.code.CfConstString;
import com.android.tools.r8.cf.code.CfFrame;
import com.android.tools.r8.cf.code.CfGoto;
import com.android.tools.r8.cf.code.CfIf;
import com.android.tools.r8.cf.code.CfIfCmp;
import com.android.tools.r8.cf.code.CfInstanceFieldRead;
import com.android.tools.r8.cf.code.CfInstanceFieldWrite;
import com.android.tools.r8.cf.code.CfInstanceOf;
import com.android.tools.r8.cf.code.CfInvoke;
import com.android.tools.r8.cf.code.CfLabel;
import com.android.tools.r8.cf.code.CfLoad;
import com.android.tools.r8.cf.code.CfNew;
import com.android.tools.r8.cf.code.CfNumberConversion;
import com.android.tools.r8.cf.code.CfReturn;
import com.android.tools.r8.cf.code.CfReturnVoid;
import com.android.tools.r8.cf.code.CfStackInstruction;
import com.android.tools.r8.cf.code.CfStaticFieldRead;
import com.android.tools.r8.cf.code.CfStore;
import com.android.tools.r8.cf.code.CfThrow;
import com.android.tools.r8.cf.code.frame.FrameType;
import com.android.tools.r8.dex.Constants;
import com.android.tools.r8.graph.CfCode;
import com.android.tools.r8.graph.DexEncodedField;
import com.android.tools.r8.graph.DexEncodedMethod;
import com.android.tools.r8.graph.DexItemFactory;
import com.android.tools.r8.graph.DexMethod;
import com.android.tools.r8.graph.FieldAccessFlags;
import com.android.tools.r8.graph.MethodAccessFlags;
import com.android.tools.r8.ir.code.If;
import com.android.tools.r8.ir.code.NumericType;
import com.android.tools.r8.ir.code.ValueType;
import com.android.tools.r8.synthesis.SyntheticProgramClassBuilder;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;

public final class VarHandleDesugaringMethods {

  public static void registerSynthesizedCodeReferences(DexItemFactory factory) {
    factory.createSynthesizedType("Ljava/lang/Byte;");
    factory.createSynthesizedType("Ljava/lang/ClassCastException;");
    factory.createSynthesizedType("Ljava/lang/Double;");
    factory.createSynthesizedType("Ljava/lang/Float;");
    factory.createSynthesizedType("Ljava/lang/IllegalArgumentException;");
    factory.createSynthesizedType("Ljava/lang/Integer;");
    factory.createSynthesizedType("Ljava/lang/Long;");
    factory.createSynthesizedType("Ljava/lang/RuntimeException;");
    factory.createSynthesizedType("Ljava/lang/Short;");
    factory.createSynthesizedType("Ljava/lang/UnsupportedOperationException;");
    factory.createSynthesizedType("Ljava/lang/invoke/VarHandle;");
    factory.createSynthesizedType("Ljava/lang/reflect/Field;");
    factory.createSynthesizedType("Lsun/misc/Unsafe;");
  }

  public static void generateDesugarVarHandleClass(
      SyntheticProgramClassBuilder builder, DexItemFactory factory) {
    builder.setInstanceFields(
        ImmutableList.of(
            DexEncodedField.syntheticBuilder()
                .setField(
                    factory.createField(
                        builder.getType(),
                        factory.createType(factory.createString("Lsun/misc/Unsafe;")),
                        factory.createString("U")))
                .setAccessFlags(FieldAccessFlags.createPublicFinalSynthetic())
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedField.syntheticBuilder()
                .setField(
                    factory.createField(
                        builder.getType(),
                        factory.createType(factory.createString("Ljava/lang/Class;")),
                        factory.createString("recv")))
                .setAccessFlags(FieldAccessFlags.createPublicFinalSynthetic())
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedField.syntheticBuilder()
                .setField(
                    factory.createField(
                        builder.getType(),
                        factory.createType(factory.createString("Ljava/lang/Class;")),
                        factory.createString("type")))
                .setAccessFlags(FieldAccessFlags.createPublicFinalSynthetic())
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedField.syntheticBuilder()
                .setField(
                    factory.createField(
                        builder.getType(), factory.longType, factory.createString("offset")))
                .setAccessFlags(FieldAccessFlags.createPublicFinalSynthetic())
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedField.syntheticBuilder()
                .setField(
                    factory.createField(
                        builder.getType(),
                        factory.longType,
                        factory.createString("arrayIndexScale")))
                .setAccessFlags(FieldAccessFlags.createPublicFinalSynthetic())
                .disableAndroidApiLevelCheck()
                .build()));
    DexMethod toIntIfPossible =
        factory.createMethod(
            builder.getType(),
            factory.createProto(factory.intType, factory.objectType, factory.booleanType),
            factory.createString("toIntIfPossible"));
    DexMethod compareAndSet =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.booleanType, factory.objectType, factory.objectType, factory.objectType),
            factory.createString("compareAndSet"));
    DexMethod getArrayInBox =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.objectType,
                factory.objectType,
                factory.intType,
                factory.createType(factory.createString("Ljava/lang/Class;"))),
            factory.createString("get"));
    DexMethod setArrayInt =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.voidType,
                factory.createType(factory.createString("[I")),
                factory.intType,
                factory.intType),
            factory.createString("set"));
    DexMethod setLong =
        factory.createMethod(
            builder.getType(),
            factory.createProto(factory.voidType, factory.objectType, factory.longType),
            factory.createString("set"));
    DexMethod boxIntIfPossible =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.objectType,
                factory.intType,
                factory.createType(factory.createString("Ljava/lang/Class;"))),
            factory.createString("boxIntIfPossible"));
    DexMethod desugarWrongMethodTypeException =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.createType(factory.createString("Ljava/lang/RuntimeException;"))),
            factory.createString("desugarWrongMethodTypeException"));
    DexMethod setArrayLong =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.voidType,
                factory.createType(factory.createString("[J")),
                factory.intType,
                factory.longType),
            factory.createString("set"));
    DexMethod getInt =
        factory.createMethod(
            builder.getType(),
            factory.createProto(factory.intType, factory.objectType),
            factory.createString("get"));
    DexMethod getArrayLong =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.longType, factory.createType(factory.createString("[J")), factory.intType),
            factory.createString("get"));
    DexMethod getLong =
        factory.createMethod(
            builder.getType(),
            factory.createProto(factory.longType, factory.objectType),
            factory.createString("get"));
    DexMethod compareAndSetArrayLong =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.booleanType,
                factory.createType(factory.createString("[J")),
                factory.intType,
                factory.longType,
                factory.longType),
            factory.createString("compareAndSet"));
    DexMethod getInBox =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.objectType,
                factory.objectType,
                factory.createType(factory.createString("Ljava/lang/Class;"))),
            factory.createString("get"));
    DexMethod get =
        factory.createMethod(
            builder.getType(),
            factory.createProto(factory.objectType, factory.objectType),
            factory.createString("get"));
    DexMethod compareAndSetArrayInt =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.booleanType,
                factory.createType(factory.createString("[I")),
                factory.intType,
                factory.intType,
                factory.intType),
            factory.createString("compareAndSet"));
    DexMethod constructor_1 =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.voidType, factory.createType(factory.createString("Ljava/lang/Class;"))),
            factory.createString("<init>"));
    DexMethod setInt =
        factory.createMethod(
            builder.getType(),
            factory.createProto(factory.voidType, factory.objectType, factory.intType),
            factory.createString("set"));
    DexMethod constructor_3 =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.voidType,
                factory.createType(factory.createString("Ljava/lang/Class;")),
                factory.createType(factory.createString("Ljava/lang/String;")),
                factory.createType(factory.createString("Ljava/lang/Class;"))),
            factory.createString("<init>"));
    DexMethod arrayRequiringNativeSupport =
        factory.createMethod(
            builder.getType(),
            factory.createProto(factory.createType(factory.createString("Ljava/lang/String;"))),
            factory.createString("arrayRequiringNativeSupport"));
    DexMethod boxLongIfPossible =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.objectType,
                factory.longType,
                factory.createType(factory.createString("Ljava/lang/Class;"))),
            factory.createString("boxLongIfPossible"));
    DexMethod compareAndSetLong =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.booleanType, factory.objectType, factory.longType, factory.longType),
            factory.createString("compareAndSet"));
    DexMethod toLongIfPossible =
        factory.createMethod(
            builder.getType(),
            factory.createProto(factory.longType, factory.objectType, factory.booleanType),
            factory.createString("toLongIfPossible"));
    DexMethod set =
        factory.createMethod(
            builder.getType(),
            factory.createProto(factory.voidType, factory.objectType, factory.objectType),
            factory.createString("set"));
    DexMethod getArrayInt =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.intType, factory.createType(factory.createString("[I")), factory.intType),
            factory.createString("get"));
    DexMethod setArray =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.voidType, factory.objectType, factory.intType, factory.objectType),
            factory.createString("set"));
    DexMethod compareAndSetInt =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.booleanType, factory.objectType, factory.intType, factory.intType),
            factory.createString("compareAndSet"));
    DexMethod compareAndSetArray =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.booleanType,
                factory.objectType,
                factory.intType,
                factory.objectType,
                factory.objectType),
            factory.createString("compareAndSet"));
    DexMethod getArray =
        factory.createMethod(
            builder.getType(),
            factory.createProto(factory.objectType, factory.objectType, factory.intType),
            factory.createString("get"));
    builder.setDirectMethods(
        ImmutableList.of(
            DexEncodedMethod.syntheticBuilder()
                .setMethod(constructor_1)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, true))
                .setCode(DesugarVarHandle_constructor_1(factory, constructor_1))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(constructor_3)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, true))
                .setCode(DesugarVarHandle_constructor_3(factory, constructor_3))
                .disableAndroidApiLevelCheck()
                .build()));
    builder.setVirtualMethods(
        ImmutableList.of(
            DexEncodedMethod.syntheticBuilder()
                .setMethod(toIntIfPossible)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_toIntIfPossible(factory, toIntIfPossible))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(compareAndSet)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_compareAndSet(factory, compareAndSet))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(getArrayInBox)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_getArrayInBox(factory, getArrayInBox))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(setArrayInt)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_setArrayInt(factory, setArrayInt))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(setLong)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_setLong(factory, setLong))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(boxIntIfPossible)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_boxIntIfPossible(factory, boxIntIfPossible))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(desugarWrongMethodTypeException)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(
                    DesugarVarHandle_desugarWrongMethodTypeException(
                        factory, desugarWrongMethodTypeException))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(setArrayLong)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_setArrayLong(factory, setArrayLong))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(getInt)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_getInt(factory, getInt))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(getArrayLong)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_getArrayLong(factory, getArrayLong))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(getLong)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_getLong(factory, getLong))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(compareAndSetArrayLong)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_compareAndSetArrayLong(factory, compareAndSetArrayLong))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(getInBox)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_getInBox(factory, getInBox))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(get)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_get(factory, get))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(compareAndSetArrayInt)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_compareAndSetArrayInt(factory, compareAndSetArrayInt))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(setInt)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_setInt(factory, setInt))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(arrayRequiringNativeSupport)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(
                    DesugarVarHandle_arrayRequiringNativeSupport(
                        factory, arrayRequiringNativeSupport))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(boxLongIfPossible)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_boxLongIfPossible(factory, boxLongIfPossible))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(compareAndSetLong)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_compareAndSetLong(factory, compareAndSetLong))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(toLongIfPossible)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_toLongIfPossible(factory, toLongIfPossible))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(set)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_set(factory, set))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(getArrayInt)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_getArrayInt(factory, getArrayInt))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(setArray)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_setArray(factory, setArray))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(compareAndSetInt)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_compareAndSetInt(factory, compareAndSetInt))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(compareAndSetArray)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_compareAndSetArray(factory, compareAndSetArray))
                .disableAndroidApiLevelCheck()
                .build(),
            DexEncodedMethod.syntheticBuilder()
                .setMethod(getArray)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarVarHandle_getArray(factory, getArray))
                .disableAndroidApiLevelCheck()
                .build()));
  }

  public static void generateDesugarMethodHandlesLookupClass(
      SyntheticProgramClassBuilder builder, DexItemFactory factory) {
    builder.setInstanceFields(ImmutableList.of());
    DexMethod findVarHandle =
        factory.createMethod(
            builder.getType(),
            factory.createProto(
                factory.createType(factory.createString("Ljava/lang/invoke/VarHandle;")),
                factory.createType(factory.createString("Ljava/lang/Class;")),
                factory.createType(factory.createString("Ljava/lang/String;")),
                factory.createType(factory.createString("Ljava/lang/Class;"))),
            factory.createString("findVarHandle"));
    DexMethod constructor_0 =
        factory.createMethod(
            builder.getType(),
            factory.createProto(factory.voidType),
            factory.createString("<init>"));
    builder.setDirectMethods(
        ImmutableList.of(
            DexEncodedMethod.syntheticBuilder()
                .setMethod(constructor_0)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, true))
                .setCode(DesugarMethodHandlesLookup_constructor_0(factory, constructor_0))
                .disableAndroidApiLevelCheck()
                .build()));
    builder.setVirtualMethods(
        ImmutableList.of(
            DexEncodedMethod.syntheticBuilder()
                .setMethod(findVarHandle)
                .setAccessFlags(
                    MethodAccessFlags.fromSharedAccessFlags(
                        Constants.ACC_PUBLIC | Constants.ACC_SYNTHETIC, false))
                .setCode(DesugarMethodHandlesLookup_findVarHandle(factory, findVarHandle))
                .disableAndroidApiLevelCheck()
                .build()));
  }

  public static CfCode DesugarMethodHandlesLookup_constructor_0(
      DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    return new CfCode(
        method.holder,
        1,
        1,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.objectType,
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfReturnVoid(),
            label1),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarMethodHandlesLookup_findVarHandle(
      DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    return new CfCode(
        method.holder,
        5,
        4,
        ImmutableList.of(
            label0,
            new CfNew(factory.createType("Ljava/lang/invoke/VarHandle;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 2),
            new CfLoad(ValueType.OBJECT, 3),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(
                        factory.voidType, factory.classType, factory.stringType, factory.classType),
                    factory.createString("<init>")),
                false),
            new CfReturn(ValueType.OBJECT),
            label1),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_constructor_1(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    CfLabel label6 = new CfLabel();
    CfLabel label7 = new CfLabel();
    CfLabel label8 = new CfLabel();
    CfLabel label9 = new CfLabel();
    CfLabel label10 = new CfLabel();
    CfLabel label11 = new CfLabel();
    CfLabel label12 = new CfLabel();
    CfLabel label13 = new CfLabel();
    CfLabel label14 = new CfLabel();
    CfLabel label15 = new CfLabel();
    CfLabel label16 = new CfLabel();
    CfLabel label17 = new CfLabel();
    CfLabel label18 = new CfLabel();
    CfLabel label19 = new CfLabel();
    return new CfCode(
        method.holder,
        4,
        4,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.objectType,
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            label1,
            new CfConstClass(factory.createType("Lsun/misc/Unsafe;")),
            new CfConstString(factory.createString("theUnsafe")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(
                        factory.createType("Ljava/lang/reflect/Field;"), factory.stringType),
                    factory.createString("getDeclaredField")),
                false),
            new CfStore(ValueType.OBJECT, 2),
            label2,
            new CfLoad(ValueType.OBJECT, 2),
            new CfConstNumber(1, ValueType.INT),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/reflect/Field;"),
                    factory.createProto(factory.voidType, factory.booleanType),
                    factory.createString("setAccessible")),
                false),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 2),
            new CfConstNull(),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/reflect/Field;"),
                    factory.createProto(factory.objectType, factory.objectType),
                    factory.createString("get")),
                false),
            new CfCheckCast(factory.createType("Lsun/misc/Unsafe;")),
            new CfInstanceFieldWrite(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            label4,
            new CfLoad(ValueType.OBJECT, 1),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.booleanType),
                    factory.createString("isArray")),
                false),
            new CfIf(If.Type.NE, ValueType.INT, label6),
            label5,
            new CfNew(factory.createType("Ljava/lang/IllegalArgumentException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfNew(factory.stringBuilderType),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfConstString(factory.createString("not an array ")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringBuilderType, factory.stringType),
                    factory.createString("append")),
                false),
            new CfLoad(ValueType.OBJECT, 1),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.stringType),
                    factory.createString("getSimpleName")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringBuilderType, factory.stringType),
                    factory.createString("append")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringType),
                    factory.createString("toString")),
                false),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/IllegalArgumentException;"),
                    factory.createProto(factory.voidType, factory.stringType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label6,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.classType),
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/reflect/Field;"))
                    })),
            new CfLoad(ValueType.OBJECT, 1),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.classType),
                    factory.createString("getComponentType")),
                false),
            new CfStore(ValueType.OBJECT, 3),
            label7,
            new CfLoad(ValueType.OBJECT, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.booleanType),
                    factory.createString("isArray")),
                false),
            new CfIf(If.Type.EQ, ValueType.INT, label10),
            label8,
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfNew(factory.stringBuilderType),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfConstString(
                factory.createString("Using a VarHandle for a multidimensional array ")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringBuilderType, factory.stringType),
                    factory.createString("append")),
                false),
            new CfLoad(ValueType.OBJECT, 0),
            label9,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.stringType),
                    factory.createString("arrayRequiringNativeSupport")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringBuilderType, factory.stringType),
                    factory.createString("append")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringType),
                    factory.createString("toString")),
                false),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType, factory.stringType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label10,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.classType),
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/reflect/Field;")),
                      FrameType.initializedNonNullReference(factory.classType)
                    })),
            new CfLoad(ValueType.OBJECT, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.booleanType),
                    factory.createString("isPrimitive")),
                false),
            new CfIf(If.Type.EQ, ValueType.INT, label14),
            new CfLoad(ValueType.OBJECT, 3),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label14),
            new CfLoad(ValueType.OBJECT, 3),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label14),
            label11,
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfNew(factory.stringBuilderType),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfConstString(factory.createString("Using a VarHandle for an array of type '")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringBuilderType, factory.stringType),
                    factory.createString("append")),
                false),
            new CfLoad(ValueType.OBJECT, 3),
            label12,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.stringType),
                    factory.createString("getName")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringBuilderType, factory.stringType),
                    factory.createString("append")),
                false),
            new CfConstString(factory.createString("' ")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringBuilderType, factory.stringType),
                    factory.createString("append")),
                false),
            new CfLoad(ValueType.OBJECT, 0),
            label13,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.stringType),
                    factory.createString("arrayRequiringNativeSupport")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringBuilderType, factory.stringType),
                    factory.createString("append")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringType),
                    factory.createString("toString")),
                false),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType, factory.stringType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label14,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.classType),
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/reflect/Field;")),
                      FrameType.initializedNonNullReference(factory.classType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 1),
            new CfInstanceFieldWrite(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            label15,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 1),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.classType),
                    factory.createString("getComponentType")),
                false),
            new CfInstanceFieldWrite(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            label16,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.intType, factory.classType),
                    factory.createString("arrayBaseOffset")),
                false),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfInstanceFieldWrite(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            label17,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.intType, factory.classType),
                    factory.createString("arrayIndexScale")),
                false),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfInstanceFieldWrite(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("arrayIndexScale"))),
            label18,
            new CfReturnVoid(),
            label19),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_constructor_3(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    CfLabel label6 = new CfLabel();
    CfLabel label7 = new CfLabel();
    CfLabel label8 = new CfLabel();
    CfLabel label9 = new CfLabel();
    CfLabel label10 = new CfLabel();
    CfLabel label11 = new CfLabel();
    CfLabel label12 = new CfLabel();
    CfLabel label13 = new CfLabel();
    return new CfCode(
        method.holder,
        4,
        6,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.objectType,
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            label1,
            new CfConstClass(factory.createType("Lsun/misc/Unsafe;")),
            new CfConstString(factory.createString("theUnsafe")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(
                        factory.createType("Ljava/lang/reflect/Field;"), factory.stringType),
                    factory.createString("getDeclaredField")),
                false),
            new CfStore(ValueType.OBJECT, 4),
            label2,
            new CfLoad(ValueType.OBJECT, 4),
            new CfConstNumber(1, ValueType.INT),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/reflect/Field;"),
                    factory.createProto(factory.voidType, factory.booleanType),
                    factory.createString("setAccessible")),
                false),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 4),
            new CfConstNull(),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/reflect/Field;"),
                    factory.createProto(factory.objectType, factory.objectType),
                    factory.createString("get")),
                false),
            new CfCheckCast(factory.createType("Lsun/misc/Unsafe;")),
            new CfInstanceFieldWrite(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            label4,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 1),
            new CfInstanceFieldWrite(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            label5,
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 2),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(
                        factory.createType("Ljava/lang/reflect/Field;"), factory.stringType),
                    factory.createString("getDeclaredField")),
                false),
            new CfStore(ValueType.OBJECT, 5),
            label6,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 5),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/reflect/Field;"),
                    factory.createProto(factory.classType),
                    factory.createString("getType")),
                false),
            new CfInstanceFieldWrite(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            label7,
            new CfLoad(ValueType.OBJECT, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.booleanType),
                    factory.createString("isPrimitive")),
                false),
            new CfIf(If.Type.EQ, ValueType.INT, label10),
            new CfLoad(ValueType.OBJECT, 3),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label10),
            new CfLoad(ValueType.OBJECT, 3),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label10),
            label8,
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfNew(factory.stringBuilderType),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfConstString(factory.createString("Using a VarHandle for a field of type '")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringBuilderType, factory.stringType),
                    factory.createString("append")),
                false),
            new CfLoad(ValueType.OBJECT, 3),
            label9,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.stringType),
                    factory.createString("getName")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringBuilderType, factory.stringType),
                    factory.createString("append")),
                false),
            new CfConstString(
                factory.createString(
                    "' requires native VarHandle support available from Android 13. VarHandle"
                        + " desugaring only supports primitive types int and long and reference"
                        + " types.")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringBuilderType, factory.stringType),
                    factory.createString("append")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.stringBuilderType,
                    factory.createProto(factory.stringType),
                    factory.createString("toString")),
                false),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType, factory.stringType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label10,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4, 5},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.classType),
                      FrameType.initializedNonNullReference(factory.stringType),
                      FrameType.initializedNonNullReference(factory.classType),
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/reflect/Field;")),
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/reflect/Field;"))
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 2),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(
                        factory.createType("Ljava/lang/reflect/Field;"), factory.stringType),
                    factory.createString("getDeclaredField")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.longType, factory.createType("Ljava/lang/reflect/Field;")),
                    factory.createString("objectFieldOffset")),
                false),
            new CfInstanceFieldWrite(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            label11,
            new CfLoad(ValueType.OBJECT, 0),
            new CfConstNumber(0, ValueType.LONG),
            new CfInstanceFieldWrite(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("arrayIndexScale"))),
            label12,
            new CfReturnVoid(),
            label13),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_arrayRequiringNativeSupport(
      DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    return new CfCode(
        method.holder,
        1,
        1,
        ImmutableList.of(
            label0,
            new CfConstString(
                factory.createString(
                    "requires native VarHandle support available from Android 13. VarHandle"
                        + " desugaring only supports single dimensional arrays of primitive"
                        + " typesint and long and reference types.")),
            new CfReturn(ValueType.OBJECT),
            label1),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_boxIntIfPossible(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    CfLabel label6 = new CfLabel();
    CfLabel label7 = new CfLabel();
    return new CfCode(
        method.holder,
        2,
        3,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 2),
            new CfConstClass(factory.createType("Ljava/lang/Long;")),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label2),
            label1,
            new CfLoad(ValueType.INT, 1),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Long;"),
                    factory.createProto(factory.createType("Ljava/lang/Long;"), factory.longType),
                    factory.createString("valueOf")),
                false),
            new CfReturn(ValueType.OBJECT),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.classType)
                    })),
            new CfLoad(ValueType.OBJECT, 2),
            new CfConstClass(factory.createType("Ljava/lang/Float;")),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label4),
            label3,
            new CfLoad(ValueType.INT, 1),
            new CfNumberConversion(NumericType.INT, NumericType.FLOAT),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Float;"),
                    factory.createProto(factory.createType("Ljava/lang/Float;"), factory.floatType),
                    factory.createString("valueOf")),
                false),
            new CfReturn(ValueType.OBJECT),
            label4,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.classType)
                    })),
            new CfLoad(ValueType.OBJECT, 2),
            new CfConstClass(factory.createType("Ljava/lang/Double;")),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label6),
            label5,
            new CfLoad(ValueType.INT, 1),
            new CfNumberConversion(NumericType.INT, NumericType.DOUBLE),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Double;"),
                    factory.createProto(
                        factory.createType("Ljava/lang/Double;"), factory.doubleType),
                    factory.createString("valueOf")),
                false),
            new CfReturn(ValueType.OBJECT),
            label6,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.classType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.createType("Ljava/lang/RuntimeException;")),
                    factory.createString("desugarWrongMethodTypeException")),
                false),
            new CfThrow(),
            label7),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_boxLongIfPossible(
      DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    return new CfCode(
        method.holder,
        2,
        4,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 3),
            new CfConstClass(factory.createType("Ljava/lang/Float;")),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label2),
            label1,
            new CfLoad(ValueType.LONG, 1),
            new CfNumberConversion(NumericType.LONG, NumericType.FLOAT),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Float;"),
                    factory.createProto(factory.createType("Ljava/lang/Float;"), factory.floatType),
                    factory.createString("valueOf")),
                false),
            new CfReturn(ValueType.OBJECT),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.longType(),
                      FrameType.longHighType(),
                      FrameType.initializedNonNullReference(factory.classType)
                    })),
            new CfLoad(ValueType.OBJECT, 3),
            new CfConstClass(factory.createType("Ljava/lang/Double;")),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label4),
            label3,
            new CfLoad(ValueType.LONG, 1),
            new CfNumberConversion(NumericType.LONG, NumericType.DOUBLE),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Double;"),
                    factory.createProto(
                        factory.createType("Ljava/lang/Double;"), factory.doubleType),
                    factory.createString("valueOf")),
                false),
            new CfReturn(ValueType.OBJECT),
            label4,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.longType(),
                      FrameType.longHighType(),
                      FrameType.initializedNonNullReference(factory.classType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.createType("Ljava/lang/RuntimeException;")),
                    factory.createString("desugarWrongMethodTypeException")),
                false),
            new CfThrow(),
            label5),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_compareAndSet(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    CfLabel label6 = new CfLabel();
    CfLabel label7 = new CfLabel();
    CfLabel label8 = new CfLabel();
    CfLabel label9 = new CfLabel();
    return new CfCode(
        method.holder,
        9,
        4,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label4),
            label1,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 2),
            new CfConstNumber(0, ValueType.INT),
            label2,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.intType, factory.objectType, factory.booleanType),
                    factory.createString("toIntIfPossible")),
                false),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 3),
            new CfConstNumber(0, ValueType.INT),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.intType, factory.objectType, factory.booleanType),
                    factory.createString("toIntIfPossible")),
                false),
            label3,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.longType,
                        factory.intType,
                        factory.intType),
                    factory.createString("compareAndSwapInt")),
                false),
            new CfReturn(ValueType.INT),
            label4,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label8),
            label5,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 2),
            new CfConstNumber(0, ValueType.INT),
            label6,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.longType, factory.objectType, factory.booleanType),
                    factory.createString("toLongIfPossible")),
                false),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 3),
            new CfConstNumber(0, ValueType.INT),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.longType, factory.objectType, factory.booleanType),
                    factory.createString("toLongIfPossible")),
                false),
            label7,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.longType,
                        factory.longType,
                        factory.longType),
                    factory.createString("compareAndSwapLong")),
                false),
            new CfReturn(ValueType.INT),
            label8,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.OBJECT, 2),
            new CfLoad(ValueType.OBJECT, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.longType,
                        factory.objectType,
                        factory.objectType),
                    factory.createString("compareAndSwapObject")),
                false),
            new CfReturn(ValueType.INT),
            label9),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_compareAndSetArray(
      DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    CfLabel label6 = new CfLabel();
    CfLabel label7 = new CfLabel();
    CfLabel label8 = new CfLabel();
    CfLabel label9 = new CfLabel();
    CfLabel label10 = new CfLabel();
    CfLabel label11 = new CfLabel();
    CfLabel label12 = new CfLabel();
    CfLabel label13 = new CfLabel();
    CfLabel label14 = new CfLabel();
    return new CfCode(
        method.holder,
        9,
        7,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.booleanType),
                    factory.createString("isArray")),
                false),
            new CfIf(If.Type.EQ, ValueType.INT, label1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.objectType,
                    factory.createProto(factory.classType),
                    factory.createString("getClass")),
                false),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label2),
            label1,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("arrayIndexScale"))),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Mul, NumericType.LONG),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Add, NumericType.LONG),
            new CfStore(ValueType.LONG, 5),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfConstClass(factory.intArrayType),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label8),
            label4,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 5),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 3),
            new CfConstNumber(0, ValueType.INT),
            label5,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.intType, factory.objectType, factory.booleanType),
                    factory.createString("toIntIfPossible")),
                false),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 4),
            new CfConstNumber(0, ValueType.INT),
            label6,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.intType, factory.objectType, factory.booleanType),
                    factory.createString("toIntIfPossible")),
                false),
            label7,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.longType,
                        factory.intType,
                        factory.intType),
                    factory.createString("compareAndSwapInt")),
                false),
            new CfReturn(ValueType.INT),
            label8,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4, 5, 6},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfConstClass(factory.longArrayType),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label13),
            label9,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 5),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 3),
            new CfConstNumber(0, ValueType.INT),
            label10,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.longType, factory.objectType, factory.booleanType),
                    factory.createString("toLongIfPossible")),
                false),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 4),
            new CfConstNumber(0, ValueType.INT),
            label11,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.longType, factory.objectType, factory.booleanType),
                    factory.createString("toLongIfPossible")),
                false),
            label12,
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.longType,
                        factory.longType,
                        factory.longType),
                    factory.createString("compareAndSwapLong")),
                false),
            new CfReturn(ValueType.INT),
            label13,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4, 5, 6},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 5),
            new CfLoad(ValueType.OBJECT, 3),
            new CfLoad(ValueType.OBJECT, 4),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.longType,
                        factory.objectType,
                        factory.objectType),
                    factory.createString("compareAndSwapObject")),
                false),
            new CfReturn(ValueType.INT),
            label14),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_compareAndSetArrayInt(
      DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    return new CfCode(
        method.holder,
        6,
        7,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfConstClass(factory.intArrayType),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label2),
            label1,
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.intArrayType),
                      FrameType.intType(),
                      FrameType.intType(),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("arrayIndexScale"))),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Mul, NumericType.LONG),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Add, NumericType.LONG),
            new CfStore(ValueType.LONG, 5),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 5),
            new CfLoad(ValueType.INT, 3),
            new CfLoad(ValueType.INT, 4),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.longType,
                        factory.intType,
                        factory.intType),
                    factory.createString("compareAndSwapInt")),
                false),
            new CfReturn(ValueType.INT),
            label4),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_compareAndSetArrayLong(
      DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    return new CfCode(
        method.holder,
        8,
        9,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfConstClass(factory.longArrayType),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label2),
            label1,
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4, 5, 6},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.longArrayType),
                      FrameType.intType(),
                      FrameType.longType(),
                      FrameType.longHighType(),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("arrayIndexScale"))),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Mul, NumericType.LONG),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Add, NumericType.LONG),
            new CfStore(ValueType.LONG, 7),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 7),
            new CfLoad(ValueType.LONG, 3),
            new CfLoad(ValueType.LONG, 5),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.longType,
                        factory.longType,
                        factory.longType),
                    factory.createString("compareAndSwapLong")),
                false),
            new CfReturn(ValueType.INT),
            label4),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_compareAndSetInt(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    return new CfCode(
        method.holder,
        8,
        4,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label2),
            label1,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfLoad(ValueType.INT, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.longType,
                        factory.intType,
                        factory.intType),
                    factory.createString("compareAndSwapInt")),
                false),
            new CfReturn(ValueType.INT),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label4),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfLoad(ValueType.INT, 3),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.longType,
                        factory.longType,
                        factory.longType),
                    factory.createString("compareAndSwapLong")),
                false),
            new CfReturn(ValueType.INT),
            label4,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.INT, 2),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.createProto(factory.createType("Ljava/lang/Integer;"), factory.intType),
                    factory.createString("valueOf")),
                false),
            new CfLoad(ValueType.INT, 3),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.createProto(factory.createType("Ljava/lang/Integer;"), factory.intType),
                    factory.createString("valueOf")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.objectType,
                        factory.objectType),
                    factory.createString("compareAndSet")),
                false),
            new CfReturn(ValueType.INT),
            label5),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_compareAndSetLong(
      DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    return new CfCode(
        method.holder,
        8,
        6,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label2),
            label1,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.LONG, 2),
            new CfLoad(ValueType.LONG, 4),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.longType,
                        factory.longType,
                        factory.longType),
                    factory.createString("compareAndSwapLong")),
                false),
            new CfReturn(ValueType.INT),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4, 5},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.longType(),
                      FrameType.longHighType(),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 2),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Long;"),
                    factory.createProto(factory.createType("Ljava/lang/Long;"), factory.longType),
                    factory.createString("valueOf")),
                false),
            new CfLoad(ValueType.LONG, 4),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Long;"),
                    factory.createProto(factory.createType("Ljava/lang/Long;"), factory.longType),
                    factory.createString("valueOf")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(
                        factory.booleanType,
                        factory.objectType,
                        factory.objectType,
                        factory.objectType),
                    factory.createString("compareAndSet")),
                false),
            new CfReturn(ValueType.INT),
            label3),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_desugarWrongMethodTypeException(
      DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    return new CfCode(
        method.holder,
        3,
        1,
        ImmutableList.of(
            label0,
            new CfNew(factory.createType("Ljava/lang/RuntimeException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfConstString(factory.createString("java.lang.invoke.WrongMethodTypeException")),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/RuntimeException;"),
                    factory.createProto(factory.voidType, factory.stringType),
                    factory.createString("<init>")),
                false),
            new CfReturn(ValueType.OBJECT),
            label1),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_get(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    return new CfCode(
        method.holder,
        4,
        2,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label2),
            label1,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.intType, factory.objectType, factory.longType),
                    factory.createString("getInt")),
                false),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.createProto(factory.createType("Ljava/lang/Integer;"), factory.intType),
                    factory.createString("valueOf")),
                false),
            new CfReturn(ValueType.OBJECT),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label4),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.longType, factory.objectType, factory.longType),
                    factory.createString("getLong")),
                false),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Long;"),
                    factory.createProto(factory.createType("Ljava/lang/Long;"), factory.longType),
                    factory.createString("valueOf")),
                false),
            new CfReturn(ValueType.OBJECT),
            label4,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.objectType, factory.objectType, factory.longType),
                    factory.createString("getObject")),
                false),
            new CfReturn(ValueType.OBJECT),
            label5),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_getArray(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    CfLabel label6 = new CfLabel();
    CfLabel label7 = new CfLabel();
    CfLabel label8 = new CfLabel();
    return new CfCode(
        method.holder,
        6,
        5,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.booleanType),
                    factory.createString("isArray")),
                false),
            new CfIf(If.Type.EQ, ValueType.INT, label1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.objectType,
                    factory.createProto(factory.classType),
                    factory.createString("getClass")),
                false),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label2),
            label1,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType()
                    })),
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("arrayIndexScale"))),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Mul, NumericType.LONG),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Add, NumericType.LONG),
            new CfStore(ValueType.LONG, 3),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label5),
            label4,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.intType, factory.objectType, factory.longType),
                    factory.createString("getInt")),
                false),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.createProto(factory.createType("Ljava/lang/Integer;"), factory.intType),
                    factory.createString("valueOf")),
                false),
            new CfReturn(ValueType.OBJECT),
            label5,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label7),
            label6,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.longType, factory.objectType, factory.longType),
                    factory.createString("getLong")),
                false),
            new CfNumberConversion(NumericType.LONG, NumericType.INT),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.createProto(factory.createType("Ljava/lang/Integer;"), factory.intType),
                    factory.createString("valueOf")),
                false),
            new CfReturn(ValueType.OBJECT),
            label7,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.objectType, factory.objectType, factory.longType),
                    factory.createString("getObject")),
                false),
            new CfReturn(ValueType.OBJECT),
            label8),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_getArrayInBox(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    CfLabel label6 = new CfLabel();
    CfLabel label7 = new CfLabel();
    CfLabel label8 = new CfLabel();
    CfLabel label9 = new CfLabel();
    CfLabel label10 = new CfLabel();
    CfLabel label11 = new CfLabel();
    CfLabel label12 = new CfLabel();
    CfLabel label13 = new CfLabel();
    return new CfCode(
        method.holder,
        6,
        7,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.booleanType),
                    factory.createString("isArray")),
                false),
            new CfIf(If.Type.EQ, ValueType.INT, label1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.objectType,
                    factory.createProto(factory.classType),
                    factory.createString("getClass")),
                false),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label2),
            label1,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.classType)
                    })),
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.classType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("arrayIndexScale"))),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Mul, NumericType.LONG),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Add, NumericType.LONG),
            new CfStore(ValueType.LONG, 4),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label5),
            label4,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 4),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.intType, factory.objectType, factory.longType),
                    factory.createString("getInt")),
                false),
            new CfLoad(ValueType.OBJECT, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.objectType, factory.intType, factory.classType),
                    factory.createString("boxIntIfPossible")),
                false),
            new CfReturn(ValueType.OBJECT),
            label5,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4, 5},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.classType),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label7),
            label6,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 4),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.longType, factory.objectType, factory.longType),
                    factory.createString("getLong")),
                false),
            new CfLoad(ValueType.OBJECT, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.objectType, factory.longType, factory.classType),
                    factory.createString("boxLongIfPossible")),
                false),
            new CfReturn(ValueType.OBJECT),
            label7,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4, 5},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.classType),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 4),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.objectType, factory.objectType, factory.longType),
                    factory.createString("getObject")),
                false),
            new CfStore(ValueType.OBJECT, 6),
            label8,
            new CfLoad(ValueType.OBJECT, 6),
            new CfInstanceOf(factory.createType("Ljava/lang/Integer;")),
            new CfIf(If.Type.EQ, ValueType.INT, label10),
            new CfLoad(ValueType.OBJECT, 3),
            new CfConstClass(factory.createType("Ljava/lang/Integer;")),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label10),
            label9,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 6),
            new CfCheckCast(factory.createType("Ljava/lang/Integer;")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.createProto(factory.intType),
                    factory.createString("intValue")),
                false),
            new CfLoad(ValueType.OBJECT, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.objectType, factory.intType, factory.classType),
                    factory.createString("boxIntIfPossible")),
                false),
            new CfReturn(ValueType.OBJECT),
            label10,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4, 5, 6},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.classType),
                      FrameType.longType(),
                      FrameType.longHighType(),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 6),
            new CfInstanceOf(factory.createType("Ljava/lang/Long;")),
            new CfIf(If.Type.EQ, ValueType.INT, label12),
            new CfLoad(ValueType.OBJECT, 3),
            new CfConstClass(factory.createType("Ljava/lang/Long;")),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label12),
            label11,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 6),
            new CfCheckCast(factory.createType("Ljava/lang/Long;")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/Long;"),
                    factory.createProto(factory.longType),
                    factory.createString("longValue")),
                false),
            new CfLoad(ValueType.OBJECT, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.objectType, factory.longType, factory.classType),
                    factory.createString("boxLongIfPossible")),
                false),
            new CfReturn(ValueType.OBJECT),
            label12,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4, 5, 6},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.classType),
                      FrameType.longType(),
                      FrameType.longHighType(),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 6),
            new CfReturn(ValueType.OBJECT),
            label13),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_getArrayInt(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    return new CfCode(
        method.holder,
        6,
        5,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfConstClass(factory.intArrayType),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label2),
            label1,
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.intArrayType),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("arrayIndexScale"))),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Mul, NumericType.LONG),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Add, NumericType.LONG),
            new CfStore(ValueType.LONG, 3),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.intType, factory.objectType, factory.longType),
                    factory.createString("getInt")),
                false),
            new CfReturn(ValueType.INT),
            label4),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_getArrayLong(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    return new CfCode(
        method.holder,
        6,
        5,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfConstClass(factory.longArrayType),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label2),
            label1,
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.longArrayType),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("arrayIndexScale"))),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Mul, NumericType.LONG),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Add, NumericType.LONG),
            new CfStore(ValueType.LONG, 3),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.longType, factory.objectType, factory.longType),
                    factory.createString("getLong")),
                false),
            new CfReturn(ValueType.LONG),
            label4),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_getInBox(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    return new CfCode(
        method.holder,
        5,
        3,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label2),
            label1,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.intType, factory.objectType, factory.longType),
                    factory.createString("getInt")),
                false),
            new CfLoad(ValueType.OBJECT, 2),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.objectType, factory.intType, factory.classType),
                    factory.createString("boxIntIfPossible")),
                false),
            new CfReturn(ValueType.OBJECT),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.classType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label4),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.longType, factory.objectType, factory.longType),
                    factory.createString("getLong")),
                false),
            new CfLoad(ValueType.OBJECT, 2),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.objectType, factory.longType, factory.classType),
                    factory.createString("boxLongIfPossible")),
                false),
            new CfReturn(ValueType.OBJECT),
            label4,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.classType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.objectType, factory.objectType, factory.longType),
                    factory.createString("getObject")),
                false),
            new CfReturn(ValueType.OBJECT),
            label5),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_getInt(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    return new CfCode(
        method.holder,
        5,
        2,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label2),
            label1,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.intType, factory.objectType, factory.longType),
                    factory.createString("getInt")),
                false),
            new CfReturn(ValueType.INT),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label4),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.createType("Ljava/lang/RuntimeException;")),
                    factory.createString("desugarWrongMethodTypeException")),
                false),
            new CfThrow(),
            label4,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.objectType, factory.objectType, factory.longType),
                    factory.createString("getObject")),
                false),
            new CfConstNumber(1, ValueType.INT),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.intType, factory.objectType, factory.booleanType),
                    factory.createString("toIntIfPossible")),
                false),
            new CfReturn(ValueType.INT),
            label5),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_getLong(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    return new CfCode(
        method.holder,
        5,
        2,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label2),
            label1,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.longType, factory.objectType, factory.longType),
                    factory.createString("getLong")),
                false),
            new CfReturn(ValueType.LONG),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label4),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.intType, factory.objectType, factory.longType),
                    factory.createString("getInt")),
                false),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfReturn(ValueType.LONG),
            label4,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(factory.objectType, factory.objectType, factory.longType),
                    factory.createString("getObject")),
                false),
            new CfConstNumber(1, ValueType.INT),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.longType, factory.objectType, factory.booleanType),
                    factory.createString("toLongIfPossible")),
                false),
            new CfReturn(ValueType.LONG),
            label5),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_set(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    CfLabel label6 = new CfLabel();
    return new CfCode(
        method.holder,
        5,
        3,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label2),
            label1,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 2),
            new CfConstNumber(0, ValueType.INT),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.intType, factory.objectType, factory.booleanType),
                    factory.createString("toIntIfPossible")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.voidType, factory.objectType, factory.intType),
                    factory.createString("set")),
                false),
            new CfGoto(label5),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label4),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 2),
            new CfConstNumber(0, ValueType.INT),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.longType, factory.objectType, factory.booleanType),
                    factory.createString("toLongIfPossible")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.voidType, factory.objectType, factory.longType),
                    factory.createString("set")),
                false),
            new CfGoto(label5),
            label4,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.OBJECT, 2),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.voidType, factory.objectType, factory.longType, factory.objectType),
                    factory.createString("putObject")),
                false),
            label5,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfReturnVoid(),
            label6),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_setArray(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    CfLabel label6 = new CfLabel();
    CfLabel label7 = new CfLabel();
    CfLabel label8 = new CfLabel();
    CfLabel label9 = new CfLabel();
    return new CfCode(
        method.holder,
        7,
        6,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.classType,
                    factory.createProto(factory.booleanType),
                    factory.createString("isArray")),
                false),
            new CfIf(If.Type.EQ, ValueType.INT, label1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.objectType,
                    factory.createProto(factory.classType),
                    factory.createString("getClass")),
                false),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label2),
            label1,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.objectType)
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("arrayIndexScale"))),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Mul, NumericType.LONG),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Add, NumericType.LONG),
            new CfStore(ValueType.LONG, 4),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfConstClass(factory.intArrayType),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label5),
            label4,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 4),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 3),
            new CfConstNumber(0, ValueType.INT),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.intType, factory.objectType, factory.booleanType),
                    factory.createString("toIntIfPossible")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.voidType, factory.objectType, factory.longType, factory.intType),
                    factory.createString("putInt")),
                false),
            new CfGoto(label8),
            label5,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4, 5},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfConstClass(factory.longArrayType),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label7),
            label6,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 4),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 3),
            new CfConstNumber(0, ValueType.INT),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.longType, factory.objectType, factory.booleanType),
                    factory.createString("toLongIfPossible")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.voidType, factory.objectType, factory.longType, factory.longType),
                    factory.createString("putLong")),
                false),
            new CfGoto(label8),
            label7,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4, 5},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 4),
            new CfLoad(ValueType.OBJECT, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.voidType, factory.objectType, factory.longType, factory.objectType),
                    factory.createString("putObject")),
                false),
            label8,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4, 5},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType(),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfReturnVoid(),
            label9),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_setArrayInt(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    return new CfCode(
        method.holder,
        6,
        6,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfConstClass(factory.intArrayType),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label2),
            label1,
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.intArrayType),
                      FrameType.intType(),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("arrayIndexScale"))),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Mul, NumericType.LONG),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Add, NumericType.LONG),
            new CfStore(ValueType.LONG, 4),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 4),
            new CfLoad(ValueType.INT, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.voidType, factory.objectType, factory.longType, factory.intType),
                    factory.createString("putInt")),
                false),
            label4,
            new CfReturnVoid(),
            label5),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_setArrayLong(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    return new CfCode(
        method.holder,
        6,
        7,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("recv"))),
            new CfConstClass(factory.longArrayType),
            new CfIfCmp(If.Type.EQ, ValueType.OBJECT, label2),
            label1,
            new CfNew(factory.createType("Ljava/lang/UnsupportedOperationException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/UnsupportedOperationException;"),
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3, 4},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.longArrayType),
                      FrameType.intType(),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("arrayIndexScale"))),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Mul, NumericType.LONG),
            new CfArithmeticBinop(CfArithmeticBinop.Opcode.Add, NumericType.LONG),
            new CfStore(ValueType.LONG, 5),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.LONG, 5),
            new CfLoad(ValueType.LONG, 3),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.voidType, factory.objectType, factory.longType, factory.longType),
                    factory.createString("putLong")),
                false),
            label4,
            new CfReturnVoid(),
            label5),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_setInt(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    CfLabel label6 = new CfLabel();
    return new CfCode(
        method.holder,
        6,
        3,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label2),
            label1,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.voidType, factory.objectType, factory.longType, factory.intType),
                    factory.createString("putInt")),
                false),
            new CfGoto(label5),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label4),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.INT, 2),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.voidType, factory.objectType, factory.longType, factory.longType),
                    factory.createString("putLong")),
                false),
            new CfGoto(label5),
            label4,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.INT, 2),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.createProto(factory.createType("Ljava/lang/Integer;"), factory.intType),
                    factory.createString("valueOf")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.voidType, factory.objectType, factory.objectType),
                    factory.createString("set")),
                false),
            label5,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType()
                    })),
            new CfReturnVoid(),
            label6),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_setLong(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    CfLabel label6 = new CfLabel();
    return new CfCode(
        method.holder,
        6,
        4,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Long;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label2),
            label1,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.LONG, 2),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.voidType, factory.objectType, factory.longType, factory.longType),
                    factory.createString("putLong")),
                false),
            new CfGoto(label5),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.classType,
                    factory.createString("type"))),
            new CfStaticFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.classType,
                    factory.createString("TYPE"))),
            new CfIfCmp(If.Type.NE, ValueType.OBJECT, label4),
            label3,
            new CfLoad(ValueType.OBJECT, 0),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.createType("Ljava/lang/RuntimeException;")),
                    factory.createString("desugarWrongMethodTypeException")),
                false),
            new CfThrow(),
            label4,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createString("U"))),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInstanceFieldRead(
                factory.createField(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.longType,
                    factory.createString("offset"))),
            new CfLoad(ValueType.LONG, 2),
            new CfInvoke(
                184,
                factory.createMethod(
                    factory.createType("Ljava/lang/Long;"),
                    factory.createProto(factory.createType("Ljava/lang/Long;"), factory.longType),
                    factory.createString("valueOf")),
                false),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Lsun/misc/Unsafe;"),
                    factory.createProto(
                        factory.voidType, factory.objectType, factory.longType, factory.objectType),
                    factory.createString("putObject")),
                false),
            label5,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2, 3},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.longType(),
                      FrameType.longHighType()
                    })),
            new CfReturnVoid(),
            label6),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_toIntIfPossible(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    CfLabel label4 = new CfLabel();
    CfLabel label5 = new CfLabel();
    CfLabel label6 = new CfLabel();
    CfLabel label7 = new CfLabel();
    CfLabel label8 = new CfLabel();
    CfLabel label9 = new CfLabel();
    CfLabel label10 = new CfLabel();
    CfLabel label11 = new CfLabel();
    return new CfCode(
        method.holder,
        2,
        3,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 1),
            new CfInstanceOf(factory.createType("Ljava/lang/Integer;")),
            new CfIf(If.Type.EQ, ValueType.INT, label2),
            label1,
            new CfLoad(ValueType.OBJECT, 1),
            new CfCheckCast(factory.createType("Ljava/lang/Integer;")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/Integer;"),
                    factory.createProto(factory.intType),
                    factory.createString("intValue")),
                false),
            new CfReturn(ValueType.INT),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 1),
            new CfInstanceOf(factory.createType("Ljava/lang/Byte;")),
            new CfIf(If.Type.EQ, ValueType.INT, label4),
            label3,
            new CfLoad(ValueType.OBJECT, 1),
            new CfCheckCast(factory.createType("Ljava/lang/Byte;")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/Byte;"),
                    factory.createProto(factory.byteType),
                    factory.createString("byteValue")),
                false),
            new CfReturn(ValueType.INT),
            label4,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 1),
            new CfInstanceOf(factory.boxedCharType),
            new CfIf(If.Type.EQ, ValueType.INT, label6),
            label5,
            new CfLoad(ValueType.OBJECT, 1),
            new CfCheckCast(factory.boxedCharType),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.boxedCharType,
                    factory.createProto(factory.charType),
                    factory.createString("charValue")),
                false),
            new CfReturn(ValueType.INT),
            label6,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 1),
            new CfInstanceOf(factory.createType("Ljava/lang/Short;")),
            new CfIf(If.Type.EQ, ValueType.INT, label8),
            label7,
            new CfLoad(ValueType.OBJECT, 1),
            new CfCheckCast(factory.createType("Ljava/lang/Short;")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/Short;"),
                    factory.createProto(factory.shortType),
                    factory.createString("shortValue")),
                false),
            new CfReturn(ValueType.INT),
            label8,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.INT, 2),
            new CfIf(If.Type.EQ, ValueType.INT, label10),
            label9,
            new CfNew(factory.createType("Ljava/lang/ClassCastException;")),
            new CfStackInstruction(CfStackInstruction.Opcode.Dup),
            new CfInvoke(
                183,
                factory.createMethod(
                    factory.createType("Ljava/lang/ClassCastException;"),
                    factory.createProto(factory.voidType),
                    factory.createString("<init>")),
                false),
            new CfThrow(),
            label10,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.createType("Ljava/lang/RuntimeException;")),
                    factory.createString("desugarWrongMethodTypeException")),
                false),
            new CfThrow(),
            label11),
        ImmutableList.of(),
        ImmutableList.of());
  }

  public static CfCode DesugarVarHandle_toLongIfPossible(DexItemFactory factory, DexMethod method) {
    CfLabel label0 = new CfLabel();
    CfLabel label1 = new CfLabel();
    CfLabel label2 = new CfLabel();
    CfLabel label3 = new CfLabel();
    return new CfCode(
        method.holder,
        3,
        3,
        ImmutableList.of(
            label0,
            new CfLoad(ValueType.OBJECT, 1),
            new CfInstanceOf(factory.createType("Ljava/lang/Long;")),
            new CfIf(If.Type.EQ, ValueType.INT, label2),
            label1,
            new CfLoad(ValueType.OBJECT, 1),
            new CfCheckCast(factory.createType("Ljava/lang/Long;")),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/Long;"),
                    factory.createProto(factory.longType),
                    factory.createString("longValue")),
                false),
            new CfReturn(ValueType.LONG),
            label2,
            new CfFrame(
                new Int2ObjectAVLTreeMap<>(
                    new int[] {0, 1, 2},
                    new FrameType[] {
                      FrameType.initializedNonNullReference(
                          factory.createType("Ljava/lang/invoke/VarHandle;")),
                      FrameType.initializedNonNullReference(factory.objectType),
                      FrameType.intType()
                    })),
            new CfLoad(ValueType.OBJECT, 0),
            new CfLoad(ValueType.OBJECT, 1),
            new CfLoad(ValueType.INT, 2),
            new CfInvoke(
                182,
                factory.createMethod(
                    factory.createType("Ljava/lang/invoke/VarHandle;"),
                    factory.createProto(factory.intType, factory.objectType, factory.booleanType),
                    factory.createString("toIntIfPossible")),
                false),
            new CfNumberConversion(NumericType.INT, NumericType.LONG),
            new CfReturn(ValueType.LONG),
            label3),
        ImmutableList.of(),
        ImmutableList.of());
  }
}
