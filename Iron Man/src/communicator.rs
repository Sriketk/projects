#[derive(Debug)]
#[derive(PartialEq)]
pub enum Command
{
    Power(bool,i32),    // [Increase/Decrease] power by [number].
    Missiles(bool,i32), // [Increase/Decrease] missiles by [number].
    Shield(bool),       // Turn [On/Off] the shield.
    Try,                // Try calling pepper.
    Invalid             // [anything else]
}


/**
    Adds functionality to Command enums
    Commands can be converted to strings with the as_str method
    
    Command     |     String format
    ---------------------------------------------------------
    Power       |  /Power (increased|decreased) by [0-9]+%/
    Missiles    |  /Missiles (increased|decreased) by [0-9]+/
    Shield      |  /Shield turned (on|off)/
    Try         |  /Call attempt failed/
    Invalid     |  /Not a command/
**/
impl Command {
    pub fn as_str (&self) -> String {
        match self {
	    Command::Power(increasing, value) => {
		if increasing == &true {
                    let mut res = String::from("Power increased by ");
                    res.push_str(&value.to_string());
                    res.push('%');
                    return res;
                } else {
                    let mut res = String::from("Power decreased by ");
                    res.push_str(&value.to_string());
                    res.push('%');
                    return res;
                }
	    },

	    Command::Missiles(increasing, value) => {
		if increasing == &true {
		    let mut res = String::from("Missiles increased by ");
                    res.push_str(&value.to_string());
                    return res;
		} else {
		    let mut res = String::from("Missiles decreased by ");
                    res.push_str(&value.to_string());
                    return res;
		}
	    },

	    Command::Shield(on) => {
		if on == &true {
		    return String::from("Shield turned on");
		} else {
		    return String::from("Shield turned off");
		}
	    },

	    Command::Try => return String::from("Call attempt failed"),
            Command::Invalid => return String::from("Not a command")
	}
    }
}

/**
    Complete this method that converts a string to a command 
    We list the format of the input strings below

    Command     |     String format
    ---------------------------------------------
    Power       |  /power (inc|dec) [0-9]+/
    Missiles    |  /(fire|add) [0-9]+ missiles/
    Shield      |  /shield (on|off)/
    Try         |  /try calling Miss Potts/
    Invalid     |  Anything else
**/
pub fn to_command(s: &str) -> Command {
    let place = s.split(' ').collect::<Vec<&str>>();
    
    if place.len() == 2 {
	if s == "shield off" {
	    return Command::Shield(false);
	} else if s == "shield on" {
	    return Command::Shield(true);
	} else {
	    return Command::Invalid;
	}
    } else if place.len() == 3 {
	if place[0] == "power" {
	    if place[1] == "inc" {
		match place[2].parse::<i32>() {
		    Ok(v) => return Command::Power(true, v),
		    Err(e) => return Command::Invalid,
		}
	    } else if place[1] == "dec" {
		match place[2].parse::<i32>() {
                    Ok(v) => return Command::Power(false, v),
                    Err(e) => return Command::Invalid,
                }
	    } else {
		return Command::Invalid;
	    }
	} else if place[0] == "fire" && place[2] == "missiles" {
	    match place[1].parse::<i32>() {
                Ok(v) => return Command::Missiles(false, v),
                Err(e) => return Command::Invalid,
            }
	} else if place[0] == "add" && place[2] == "missiles" {
	    match place[1].parse::<i32>() {
                Ok(v) => return Command::Missiles(true, v),
                Err(e) => return Command::Invalid,
            }
	} else {
	    return Command::Invalid;
	}
    } else if s == "try calling Miss Potts" {
	return Command::Try;
    } else {
	return Command::Invalid;
    }
}
