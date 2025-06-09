use jni::JNIEnv;
use jni::objects::{JObject, JClass};
use jni::sys::{jdoubleArray, JNI_VERSION_1_6, jint};

#[no_mangle]
pub extern "system" fn Java_com_seuprojeto_MainActivity_startRust(
    env: JNIEnv,
    activity: JObject,
) {
    std::thread::spawn(move || {
        loop {
            if let Some((lat, lon)) = get_coords(&env, &activity) {
                println!("Lat: {}, Lon: {}", lat, lon);
                // Aqui você pode mandar pro Slint atualizar a UI
            }
            std::thread::sleep(std::time::Duration::from_secs(2));
        }
    });
}

fn get_coords(env: &JNIEnv, activity: &JObject) -> Option<(f64, f64)> {
    let result = env
        .call_method(activity, "getLocation", "()[D", &[])
        .ok()?
        .l()
        .ok()?;

    let array = jni::objects::JDoubleArray::from(result);
    let coords: Vec<f64> = env.get_array_elements::<T>(array, jni::ReleaseMode::NoCopyBack).ok()?.as_ptr().to_vec();

    if coords.len() >= 2 {
        Some((coords[0], coords[1]))
    } else {
        None
    }
}
    