
#![no_main]
#![no_std]

extern crate alloc;

use alloc::rc::Rc;
use core::cell::RefCell;

use slint::android::android_activity::AndroidApp;
use slint::{ComponentHandle, SharedString};

#[cfg(target_os = "android")]
slint::slint! {
    export * from "ui/mainwindow.slint";
}

#[derive(Default)]
struct CalcState {
    prev_value: i32,
    current_value: i32,
    operation: SharedString,
}

#[unsafe(no_mangle)]
fn android_main(app: AndroidApp) {
    slint::android::init(app).unwrap();

    let window = MainWindow::new().unwrap();
    let weak = window.as_weak();
    let state = Rc::new(RefCell::new(CalcState::default()));

    window.global::<CalcLogic>().on_button_pressed({
        let state = state.clone();
        move |value| {
            let Some(window) = weak.upgrade() else { return };

            let mut state = state.borrow_mut();

            if let Ok(num) = value.parse::<i32>() {
                state.current_value = state.current_value * 10 + num;
                window.set_value(state.current_value);
                return;
            }

            if value.as_str() == "=" {
                let result = match state.operation.as_str() {
                    "+" => state.prev_value + state.current_value,
                    "-" => state.prev_value - state.current_value,
                    "*" => state.prev_value * state.current_value,
                    "/" => {
                        if state.current_value != 0 {
                            state.prev_value / state.current_value
                        } else {
                            0
                        }
                    }
                    _ => return,
                };
                window.set_value(result);
                state.prev_value = result;
                state.current_value = 0;
                //state.operation.clear();
            } else {
                state.operation = value.into();
                state.prev_value = state.current_value;
                state.current_value = 0;
            }
        }
    });

    window.run().unwrap();
}

use jni::JNIEnv;
use jni::objects::{JObject, JValue};
use jni::sys::jdoubleArray;

/// Função JNI chamada de uma thread no Rust
fn get_coords(env: &JNIEnv, activity: &JObject) -> Option<(f64, f64)> {
    let array_obj = env
        .call_method(*activity, "getLocation", "()[D", &[])
        .ok()?
        .l()
        .ok()?;

    let array: jdoubleArray = array_obj.into_inner();
    let elements = env.get_double_array_elements(array).ok()?;
    let slice = elements.as_ptr();

    unsafe {
        let lat = *slice;
        let lon = *slice.add(1);
        Some((lat, lon))
    }
}
