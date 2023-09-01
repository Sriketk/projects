/**
    Returns the sum 1 + 2 + ... + n
    If n is less than 0, return -1
**/
pub fn gauss(n: i32) -> i32 {
    if n < 0 {
        return -1;
    } else if n == 0 {
        return 0;
    }
    return n + gauss(n - 1)
}

/**
    Returns the number of elements in the list that 
    are in the range [s,e]
**/
pub fn in_range(ls: &[i32], s: i32, e: i32) -> i32 {
    let mut number = 0;
    for i in 0..(ls.len()) {
	    if ls[i] >= s && ls[i] <= e {
	        number += 1;
	    }
    }
    return number;
}

/**
    Returns true if target is a subset of set, false otherwise

    Ex: [1,3,2] is a subset of [1,2,3,4,5]
**/
pub fn subset<T: PartialEq>(set: &[T], target: &[T]) -> bool {
    let mut truth = false;
    for element in target.iter() {
	    for e in set.iter() {
	        if element == e {
	    	    truth = true;
	        }
	    }
	    if truth == false {
	        return false;
	    } else {
	        truth = false;
	    }
    }
    return true;
}

/**
    Returns the mean of elements in ls. If the list is empty, return None
    It might be helpful to use the fold method of the Iterator trait
**/
pub fn mean(ls: &[f64]) -> Option<f64> {
    let numbers = ls.len();
    if numbers == 0 {
    	return None;
    }

    let mut addit = 0.0;
    for element in ls.iter() {
	    addit += element;
    }

    return Some(addit / numbers as f64)
}

/**
    Converts a binary number to decimal, where each bit is stored in order in the array
    
    Ex: to_decimal of [1,0,1,0] returns 10
**/
pub fn to_decimal(ls: &[i32]) -> i32 {
    let mut dec = 0;
    for i in 0..(ls.len()) {
        if ls[i] == 1 {
            dec += 2_i32.pow((ls.len() - 1 - i) as u32);
        }
    }
    return dec;
}

/**
    Decomposes an integer into its prime factors and returns them in a vector
    You can assume factorize will never be passed anything less than 2

    Ex: factorize of 36 should return [2,2,3,3] since 36 = 2 * 2 * 3 * 3
**/
pub fn factorize(n: u32) -> Vec<u32> {
    let mut result = Vec::new();
    match (2..n+1).find(|x| n%x == 0) {
        Some(x) => {
            result.push(x);
            result.append(&mut factorize(n/x));
        },
        None => {}
    }
    return result
}

/** 
    Takes all of the elements of the given slice and creates a new vector.
    The new vector takes all the elements of the original and rotates them, 
    so the first becomes the last, the second becomes first, and so on.
    
    EX: rotate [1,2,3,4] returns [2,3,4,1]
**/
pub fn rotate(lst: &[i32]) -> Vec<i32> {
    let mut index = Vec::new();
    for i in 0..(lst.len()) {
        index.push(lst[(1 +i) % (lst.len())])
    }
    
    return index;
}

/**
    Returns true if target is a subtring of s, false otherwise
    You should not use the contains function of the string library in your implementation
    
    Ex: "ace" is a substring of "rustacean"
**/
pub fn substr(s: &String, target: &str) -> bool {
    let len_target = target.len();
    let s_length = 1 + s.len();
    for i in len_target..s_length {
        if s[(i - len_target)..i].eq(target) {
            return true
        }
    }
    return false
}

/**
    Takes a string and returns the first longest substring of consecutive equal characters

    EX: longest_sequence of "ababbba" is Some("bbb")
    EX: longest_sequence of "aaabbb" is Some("aaa")
    EX: longest_sequence of "xyz" is Some("x")
    EX: longest_sequence of "" is None
**/
pub fn longest_sequence(s: &str) -> Option<&str> {
    let mut len = 0;
    let mut order = "";
    for i in 0..s.len() {
        for j in i+1..s.len()+1 {
            if similar(s, i, j) && len < j - i   {
                len = j - i;
                order = &s[i..j];
            }
        }
    } 
    if len > 0 {
        return Some(&order)
    } else {
        return None
    }
}

pub fn similar(s: &str, start: usize, end: usize) -> bool {
    return s[start..end].chars().all(|x| x == s[start..end].chars().next().unwrap());
}
