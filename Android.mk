
ifeq ($(BUILD_WITH_TOFU_BOOTSETTINGS),true)

LIB_TARGET_PREFIX := tofubootsettings_
LOCAL_PATH        := $(call my-dir)

define _add-lib
include $(CLEAR_VARS)
LOCAL_MODULE                := $(LIB_TARGET_PREFIX)$1
LOCAL_MODULE_STEM           := $1
LOCAL_MODULE_TAGS           := optional
LOCAL_MODULE_SUFFIX         := .so
LOCAL_MODULE_CLASS          := SHARED_LIBRARIES
LOCAL_MODULE_PATH           := $(TARGET_OUT_SHARED_LIBRARIES)
LOCAL_SRC_FILES             := libs/armeabi/$1.so
OVERRIDE_BUILT_MODULE_PATH  := $(TARGET_OUT_INTERMEDIATE_LIBRARIES)
include $(BUILD_PREBUILT)
endef

prebuilt_app_libs := $(shell find $(LOCAL_PATH)/libs -name *.so)
prebuilt_app_modules := \
  $(foreach _file,$(prebuilt_app_libs),\
    $(notdir $(basename $(_file))))

# avoid $ being expanded in the sed expression below
REGEX_END_OF_STRING := $

prebuilt_app_java_libs := $(shell find $(LOCAL_PATH)/libs -name *.jar)
prebuilt_app_java_module_names := \
  $(foreach _file,$(prebuilt_app_java_libs),\
    $(LIB_TARGET_PREFIX)$(notdir $(shell echo $(basename $(_file)) | sed -e 's/-[0-9.]\+$(REGEX_END_OF_STRING)//')))
prebuilt_app_java_module_defs := \
  $(foreach _file,$(prebuilt_app_java_libs),\
    $(LIB_TARGET_PREFIX)$(notdir $(shell echo $(basename $(_file)) | sed -e 's/-[0-9.]\+$(REGEX_END_OF_STRING)//')):libs/$(notdir $(_file)))

include $(CLEAR_VARS)
LOCAL_MODULE            := $(LIB_TARGET_PREFIX)libs
LOCAL_MODULE_TAGS       := optional
LOCAL_REQUIRED_MODULES  := $(addprefix $(LIB_TARGET_PREFIX),$(prebuilt_app_modules))
include $(BUILD_PHONY_PACKAGE)

$(foreach _module,$(prebuilt_app_modules),\
  $(eval $(call _add-lib,$(_module))))

include $(CLEAR_VARS)
LOCAL_PACKAGE_NAME          := TofuBootSettings
LOCAL_MODULE_TAGS           := optional
LOCAL_SRC_FILES             := $(call all-subdir-java-files)
LOCAL_MODULE_PATH           := $(TARGET_OUT_APPS)
LOCAL_CERTIFICATE           := platform
LOCAL_PROGUARD_ENABLED := disabled
LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4 $(prebuilt_app_java_module_names)
LOCAL_REQUIRED_MODULES      := $(LIB_TARGET_PREFIX)libs
include $(BUILD_PACKAGE)

include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := $(prebuilt_app_java_module_defs)
include $(BUILD_MULTI_PREBUILT)

endif

