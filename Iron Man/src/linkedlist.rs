use std::{
    borrow::BorrowMut,
    ops::{Deref, DerefMut},
    sync::{Arc, RwLock},
};

use std::mem;

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum Component {
    Helmet(bool),              //is damaged?
    LeftThrusters(bool, i32),  //is damaged? How much power left?
    RightThrusters(bool, i32), //is damaged? How much power left?
    LeftRepulsor(bool, i32),   //is damaged? How much power left?
    RightRepulsor(bool, i32),  //is damaged? How much power left?
    ChestPiece(bool, i32),     //is damaged? How much power left?
    Missiles(i32),             //how many missiles left?
    ArcReactor(i32),           // How much power left?
    Wifi(bool),                // connected to wifi?
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub struct Armor {
    pub component: Component,
    pub version: i32,
}

// Part 2

// Students should fill in the Link type themselves. The Node and List types are given as is.
type Link = (Option<Arc<RwLock<Node>>>);

struct Node {
    data: Armor,
    rest: Link,
}

#[derive(Clone)]
pub struct List {
    head_link: Link,
    size: usize,
}

impl List {
    pub fn new() -> Self {
        List {head_link: None, size: 0}
    }

    pub fn size(&self) -> usize {
        self.size
    }

    pub fn peek(&self) -> Option<Armor> {
        if self.size == 0 {
            return None
        } else {
            return Some(self.head_link.clone().unwrap().read().unwrap().data.clone())
        }
    }

    pub fn push(&mut self, component: Armor) {
        let blank = Arc::new(RwLock::new(Node {
            data: component,
            rest: mem::replace(&mut self.head_link, None),
        }));
        self.size = self.size()+1;
        self.head_link = Some(blank);
    }

    pub fn pop(&mut self) -> Option<Armor> {
        match mem::replace(&mut self.head_link, None) {
            None => None,
            Some(node) => {
                self.size = self.size()-1;
                self.head_link = node.read().unwrap().rest.clone();
                Some(node.read().unwrap().data)
            }
        }
    }
}

// Part 3

#[derive(Clone)]
pub struct Suit {
    pub armor: List,
    pub version: i32,
}

impl Suit {
    pub fn is_compatible(&self) -> bool {
        if self.armor.size <= 0 {
            return false
        } else {
            let mut cloned_armor: List = self.armor.clone();
            while !(cloned_armor.peek().is_none()) {
                if cloned_armor.peek().unwrap().version != self.version {
                    return false
                }
                cloned_armor.pop();
            }
            return true
        }
    }

    pub fn repair(&mut self) {
        let mut cloned_armor= self.armor.clone();
        while !cloned_armor.head_link.is_none() {
        {
            let mut stark = cloned_armor.head_link.as_ref().unwrap().write().unwrap();
            match &mut stark.data.component {
                Component::RightRepulsor(x, y) =>
                if *x == true {
                    *y = 100;
                    *x = false;
                },
                Component::RightThrusters(x, y) =>
                if *x == true {
                    *y = 100;
                    *x = false;
                },
                Component::LeftRepulsor(x, y) =>
                if *x == true {
                    *y = 100;
                    *x = false;
                },
                Component::LeftThrusters(x, y) =>
                if *x == true {
                    *y = 100;
                    *x = false;
                },
                Component::ChestPiece(x, y) =>
                if *x == true {
                    *y = 100;
                    *x = false;
                },
                _ => {}
            }
        }
            cloned_armor.pop();
        }
    }
}
