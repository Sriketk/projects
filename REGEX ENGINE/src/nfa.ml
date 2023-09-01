open List
open Sets

(*********)
(* Types *)
(*********)

type ('q, 's) transition = 'q * 's option * 'q

type ('q, 's) nfa_t = {
  sigma: 's list;
  qs: 'q list;
  q0: 'q;
  fs: 'q list;
  delta: ('q, 's) transition list;
}

(***********)
(* Utility *)
(***********)

(* explode converts a string to a character list *)
let explode (s: string) : char list =
  let rec exp i l =
    if i < 0 then l else exp (i - 1) (s.[i] :: l)
  in
  exp (String.length s - 1) []

(****************)
(* Part 1: NFAs *)
(****************)

let move (nfa: ('q,'s) nfa_t) (qs: 'q list) (s: 's option) : 'q list = (* this is to see if it moves*)
  let check_transition d e = 
  List.fold_left (fun a b -> match b with | (x, y, state) -> if List.mem state a then a else if x = e && s = y then state::a else a) d nfa.delta
  in
  List.fold_left check_transition [] qs

let e_closure (nfa: ('q,'s) nfa_t) (qs: 'q list) : 'q list =  (* this is for eclosure*)
  let rec e_closure_helper (nfa: ('q, 's) nfa_t) (qs: 'q list) : 'q list =
	let check_transition first second = List.fold_left (fun third arg -> match arg with
    	| (fifth, sixth, state) -> if fifth = second && sixth = None then 
		if List.mem state third then union third (e_closure_helper nfa [state]) 
		else (union (state::third) (e_closure_helper nfa [state])) else third) first nfa.delta
    in
	List.fold_left check_transition qs qs
  in
  e_closure_helper nfa qs

let rec has x y = match x with (* to see if it contains*)
  | [] -> false
  |a::b -> if (List.mem a y) then true else has b y

let accept (nfa: ('q,char) nfa_t) (s: string) : bool =  (* this is for accepting*)
  let rec accept_helper x y = match y with 
    | [] -> let fstates = (e_closure nfa x) 
      in if (has fstates nfa.fs) then true else false
    | a::b -> accept_helper (move nfa (e_closure nfa x) (Some a)) b
  in
  accept_helper ([nfa.q0]) (explode s)


(*******************************)
(* Part 2: Subset Construction *)
(*******************************)

let new_states (nfa: ('q,'s) nfa_t) (qs: 'q list) : 'q list list = (* this is for all the new states*)
  List.rev (List.fold_left (fun one two -> ((List.fold_left (fun m n -> match n with 
	  | (x, Some y, state) -> if (List.mem x qs) && (y = two) then List.append (e_closure nfa [state]) m else m 
	  | (_, _, _) -> m) [] nfa.delta)::one)) [] nfa.sigma)

let new_trans (nfa: ('q,'s) nfa_t) (qs: 'q list) : ('q list, 's) transition list = (* this is a new trans *)
  let rec new_trans_helper first second = match first with
    | [] -> []
    | x::y -> (qs, Some (List.hd second), x)::(new_trans_helper y (List.tl second))
  in
  new_trans_helper (new_states nfa qs) (nfa.sigma)

let new_finals (nfa: ('q,'s) nfa_t) (qs: 'q list) : 'q list list =
  if (List.fold_left (fun x y -> x || (List.mem y nfa.fs)) false qs) then [qs] 
  else []


let rec nfa_to_dfa_step (nfa: ('q,'s) nfa_t) (dfa: ('q list, 's) nfa_t)
    (work: 'q list list) : ('q list, 's) nfa_t = failwith "unimplemented"

let nfa_to_dfa (nfa: ('q,'s) nfa_t) : ('q list, 's) nfa_t =
  let dfa_q0 = e_closure nfa [nfa.q0] in   
  let rec state_helper marked unmarked =
	let poss_moves first now = List.fold_left (fun a x -> e_closure nfa (move nfa (e_closure nfa now) (Some x))::a) [] first in
    	match unmarked with
            | [] -> marked
            | a::b -> if (List.mem a marked) = false then let unmark = poss_moves nfa.sigma a in state_helper (marked@[a]) (b@unmark) else state_helper marked b 
  in 
  let dfa_qs = state_helper [] [dfa_q0] in   
  let rec transition_helper unmarked =
    let rec temp second rece = match second with
          | [] -> []
          | h3::t3 -> if (move nfa (e_closure nfa rece) (Some h3)) != [] then ((e_closure nfa rece), (Some h3), e_closure nfa (move nfa (e_closure nfa rece) (Some h3)))::temp t3 rece else temp t3 rece
    in
    let poss_trans cur = List.fold_left (fun a x -> (temp nfa.sigma x)@a) [] cur in
	List.fold_left (fun t q -> (poss_trans q)@t) [] unmarked
  in
  let dfa_delta = transition_helper [dfa_qs] in 
  let dfa_fs = List.fold_left (fun z c -> if has nfa.fs c then c::z else z) [] dfa_qs in 
  let dfa = {
	qs = dfa_qs; 
	sigma = nfa.sigma; 
	delta = dfa_delta; 
	q0 = dfa_q0; 
	fs = dfa_fs
  } in dfa 

