open List
open Nfa

(*********)
(* Types *)
(*********)

type regexp_t =
  | Empty_String
  | Char of char
  | Union of regexp_t * regexp_t
  | Concat of regexp_t * regexp_t
  | Star of regexp_t

(***********)
(* Utility *)
(***********)

let fresh =
  let cntr = ref 0 in
  fun () ->
    cntr := !cntr + 1 ;
    !cntr



(*******************************)
(* Part 3: Regular Expressions *)
(*******************************)


let adj_nfa nfa1 nfa2 =  (* for an adjacen nfa*)
  let nstates = List.length nfa1.qs in 
  let adj_qs = List.map (fun a -> a + nstates) nfa2.qs in
  let rec adj_delta num_states deltas = List.map (fun a -> match a with (i, l, s) -> (i + num_states, l, s + num_states)) deltas in
  let d = adj_delta nstates nfa2.delta in
    {qs = adj_qs; sigma = nfa2.sigma; delta = d; q0=nfa2.q0+nstates; fs= [(List.hd nfa2.fs) + nstates]}  

let rec elem one two = (* elements*)
  match two with
  | a::b -> (a = one) || (elem one b)
  | [] -> false

let rec insert x y = (* to add*)
  if not (elem x y) then x::y 
  else y

let rec union x y = (*combien like union *)
  match x with
  | a::b -> insert a (union b y)
  | [] ->
    (match y with
     | a::b -> insert a (union [] b)
     | [] -> [])
       

let rec regexp_to_nfa_helper (regexp: regexp_t) : (int, char) nfa_t = match regexp with 
  | Empty_String -> (* this is for an empty string*)
	  { qs=[0]; 
    sigma=[]; 
    delta=[(0,None,0)]; 
    q0 = 0;
    fs=[0] }
  | Char(c) -> (* this is for the char*)
    { qs=[1;2];   
    sigma=[c]; 
    delta= [(1,Some c,2)]; 
    q0 = 1; 
    fs=[2] }
  | Union(r1, r2) -> (* this is for the union*)
    let n1 = regexp_to_nfa_helper r1 in 
    let n2 = regexp_to_nfa_helper r2 in 
    let adj_n2 = adj_nfa n1 n2 in
    let l2 = List.length adj_n2.qs in 
    let l1 = List.length n1.qs in
	  let new_q0 = 1 + l1 + l2 in
	  let new_fs = new_q0 + 1 in
      { qs = union (union n1.qs adj_n2.qs) [new_q0; new_fs]; 
      sigma = union n1.sigma adj_n2.sigma; 
      delta = union (union n1.delta adj_n2.delta) [(new_q0, None, n1.q0); (new_q0, None, adj_n2.q0); ((List.hd n1.fs), None, new_fs); ((List.hd adj_n2.fs), None, new_fs)];
      q0 = new_q0;
      fs = [new_fs]}  
  | Concat(r1, r2) -> (*this is for a union *)
    	let n1 = regexp_to_nfa_helper r1 in 
      	let n2 = adj_nfa n1 (regexp_to_nfa_helper r2) in
        { qs = union n1.qs n2.qs; 
        sigma = union n1.sigma n2.sigma;
        delta = union (union n1.delta n2.delta) [(List.hd n1.fs, None, n2.q0)];
        q0 = n1.q0;
        fs = n2.fs }
  | Star(r) -> (*this iss for  asstar *)
    	let n_f = regexp_to_nfa_helper r in 
      let nf_length = List.length n_f.qs in 
	    let new_q0 = 1 + nf_length in
	    let new_fs = 1 + new_q0 in
        { qs = union n_f.qs [new_q0; new_fs];
        sigma = n_f.sigma;
        delta = union n_f.delta [(new_q0, None, n_f.q0); (new_q0, None, new_fs); (List.hd n_f.fs, None, new_fs); (new_fs, None, new_q0)];
        q0 = new_q0;
        fs = [new_fs] }


let regexp_to_nfa (regexp: regexp_t) : (int, char) nfa_t = 
  regexp_to_nfa_helper regexp

(*****************************************************************)
(* Below this point is parser code that YOU DO NOT NEED TO TOUCH *)
(*****************************************************************)

exception IllegalExpression of string

(* Scanner *)
type token =
  | Tok_Char of char
  | Tok_Epsilon
  | Tok_Union
  | Tok_Star
  | Tok_LParen
  | Tok_RParen
  | Tok_END

let tokenize str =
  let re_var = Str.regexp "[a-z]" in
  let re_epsilon = Str.regexp "E" in
  let re_union = Str.regexp "|" in
  let re_star = Str.regexp "*" in
  let re_lparen = Str.regexp "(" in
  let re_rparen = Str.regexp ")" in
  let rec tok pos s =
    if pos >= String.length s then [Tok_END]
    else if Str.string_match re_var s pos then
      let token = Str.matched_string s in
      Tok_Char token.[0] :: tok (pos + 1) s
    else if Str.string_match re_epsilon s pos then
      Tok_Epsilon :: tok (pos + 1) s
    else if Str.string_match re_union s pos then Tok_Union :: tok (pos + 1) s
    else if Str.string_match re_star s pos then Tok_Star :: tok (pos + 1) s
    else if Str.string_match re_lparen s pos then Tok_LParen :: tok (pos + 1) s
    else if Str.string_match re_rparen s pos then Tok_RParen :: tok (pos + 1) s
    else raise (IllegalExpression ("tokenize: " ^ s))
  in
  tok 0 str

let tok_to_str t =
  match t with
  | Tok_Char v -> Char.escaped v
  | Tok_Epsilon -> "E"
  | Tok_Union -> "|"
  | Tok_Star -> "*"
  | Tok_LParen -> "("
  | Tok_RParen -> ")"
  | Tok_END -> "END"

(*
   S -> A Tok_Union S | A
   A -> B A | B
   B -> C Tok_Star | C
   C -> Tok_Char | Tok_Epsilon | Tok_LParen S Tok_RParen

   FIRST(S) = Tok_Char | Tok_Epsilon | Tok_LParen
   FIRST(A) = Tok_Char | Tok_Epsilon | Tok_LParen
   FIRST(B) = Tok_Char | Tok_Epsilon | Tok_LParen
   FIRST(C) = Tok_Char | Tok_Epsilon | Tok_LParen
 *)

let parse_regexp (l : token list) =
  let lookahead tok_list =
    match tok_list with
    | [] -> raise (IllegalExpression "lookahead")
    | h :: t -> (h, t)
  in
  let rec parse_S l =
    let a1, l1 = parse_A l in
    let t, n = lookahead l1 in
    match t with
    | Tok_Union ->
        let a2, l2 = parse_S n in
        (Union (a1, a2), l2)
    | _ -> (a1, l1)
  and parse_A l =
    let a1, l1 = parse_B l in
    let t, n = lookahead l1 in
    match t with
    | Tok_Char c ->
        let a2, l2 = parse_A l1 in
        (Concat (a1, a2), l2)
    | Tok_Epsilon ->
        let a2, l2 = parse_A l1 in
        (Concat (a1, a2), l2)
    | Tok_LParen ->
        let a2, l2 = parse_A l1 in
        (Concat (a1, a2), l2)
    | _ -> (a1, l1)
  and parse_B l =
    let a1, l1 = parse_C l in
    let t, n = lookahead l1 in
    match t with Tok_Star -> (Star a1, n) | _ -> (a1, l1)
  and parse_C l =
    let t, n = lookahead l in
    match t with
    | Tok_Char c -> (Char c, n)
    | Tok_Epsilon -> (Empty_String, n)
    | Tok_LParen ->
        let a1, l1 = parse_S n in
        let t2, n2 = lookahead l1 in
        if t2 = Tok_RParen then (a1, n2)
        else raise (IllegalExpression "parse_C 1")
    | _ -> raise (IllegalExpression "parse_C 2")
  in
  let rxp, toks = parse_S l in
  match toks with
  | [Tok_END] -> rxp
  | _ -> raise (IllegalExpression "parse didn't consume all tokens")


let string_to_regexp str = parse_regexp @@ tokenize str

let string_to_nfa str = regexp_to_nfa @@ string_to_regexp str
