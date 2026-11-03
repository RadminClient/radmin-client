#!/usr/bin/env python3
"""Compare decompiled v1 jar source (dec/radmin/client) with upstream meteor 26.1 tree.

Outputs under dec/_diff:
  report.txt  - CHANGED/NEW entries
  patches/    - unified diff per changed file
"""
import os
import re
import shutil
import difflib
from difflib import SequenceMatcher

DEC = os.path.join("dec", "radmin", "client")
UP_MAIN = os.path.join("w261", "src", "main", "java", "meteordevelopment", "meteorclient")
UP_LAUNCHER = os.path.join("w261", "src", "launcher", "java", "meteordevelopment", "meteorclient")
OUT = os.path.join("dec", "_diff")


def apply_renames(t):
    t = t.replace("meteordevelopment.meteorclient", "\x01")
    t = t.replace("radmin.client", "\x01")
    t = t.replace("\x01", "__PKG__")
    t = t.replace("meteor-client", "\x02")
    t = t.replace("radmin-client", "\x02")
    t = t.replace("\x02", "__MODID__")
    return t


def find_up(rel):
    for base in (UP_MAIN, UP_LAUNCHER):
        p = os.path.join(base, rel.replace("/", os.sep))
        if os.path.isfile(p):
            return p
    return None


def norm(path):
    try:
        with open(path, encoding="utf-8", errors="replace") as fh:
            text = fh.read()
    except OSError:
        return None
    text = re.sub(r"//.*", "", text)
    text = re.sub(r"/\*.*?\*/", "", text, flags=re.S)
    # package and import statements carry rename/CFR noise only
    text = re.sub(r"^\s*package\s+[\w\.\$]+;", "", text, flags=re.M)
    text = re.sub(r"^\s*import\s+(static\s+)?[\w\.\$\*]+;", "", text, flags=re.M)
    # CFR materializes implicit empty varargs into explicit empty arrays
    text = re.sub(r",\s*new\s+[A-Za-z_$][\w$<>]*\s*\[\s*0\s*\]", "", text)
    # javac inlines constants; CFR shows the numeric value
    text = re.sub(r"\bSINGLE_SUCCESS\b", "1", text)
    # collapse qualified dotted name chains to their last identifier
    text = re.sub(r"\b(?:[A-Za-z_$][\w$]*\.)+([A-Za-z_$][\w$]*)", r"\1", text)
    # annotations (@Override etc.) are largely compiler-implied; CFR omits most
    text = re.sub(r"@(?:[A-Za-z_$][\w$]*\.)+[A-Za-z_$][\w$]*", "", text)
    text = re.sub(r"@[A-Za-z_$][\w$]*(?:\([^)]*\))?", "", text)
    # casts: CFR emits them for compiler-generated generic inference; drop all
    text = re.sub(r"\(([A-Za-z_$][\w$<>\.\,\s\?\[\]]*)\)\s*", "", text)
    # anonymize lambda parameters (decompiler may rename them)
    text = re.sub(r"\((?:[^()]*)\)\s*->", "->", text)
    text = re.sub(r"\b[A-Za-z_$][\w$]*\s*->", "->", text)
    # decompiler wraps single statements in braces; block layout is not semantic
    text = re.sub(r"[{}]", "", text)
    # separators vary a lot between source and decompiler output
    text = re.sub(r"[;,]+", ",", text)
    text = re.sub(r"\s+", "", text)
    return apply_renames(text)


def main():
    if os.path.isdir(OUT):
        shutil.rmtree(OUT)
    os.makedirs(os.path.join(OUT, "patches"), exist_ok=True)
    report = []
    changed = noise = new_count = 0
    for root, _dirs, files in os.walk(DEC):
        files.sort()
        for fn in files:
            if not fn.endswith(".java"):
                continue
            dpath = os.path.join(root, fn)
            rel = os.path.relpath(dpath, DEC).replace(os.sep, "/")
            upath = find_up(rel)
            if upath is None:
                new_count += 1
                report.append("NEW " + rel)
                continue
            a_text = norm(upath)
            b_text = norm(dpath)
            if a_text is None or b_text is None:
                report.append("ERROR reading " + rel)
                continue
            a_bag = token_bag(a_text)
            b_bag = token_bag(b_text)
            if a_bag == b_bag:
                noise += 1
                continue
            changed += 1
            details = bag_diff(a_bag, b_bag)
            report.append("CHANGED " + rel + " " + details)
            with open(upath, encoding="utf-8", errors="replace") as fh:
                a = fh.read()
            with open(dpath, encoding="utf-8", errors="replace") as fh:
                b = fh.read()
            diff = difflib.unified_diff(
                a.splitlines(keepends=True),
                b.splitlines(keepends=True),
                fromfile="upstream261/" + rel,
                tofile="v1decompiled/" + rel,
            )
            pname = rel.replace("/", "__") + ".diff"
            ppath = os.path.join(OUT, "patches", pname)
            with open(ppath, "w", encoding="utf-8", newline="\n") as fh:
                fh.writelines(diff)
    report.sort()
    report.insert(0, "CHANGED={0} NOISE={1} NEW={2}".format(changed, noise, new_count))
    with open(os.path.join(OUT, "report.txt"), "w", encoding="utf-8", newline="\n") as fh:
        fh.write("\n".join(report) + "\n")
    print("done changed={0} noise={1} new={2}".format(changed, noise, new_count))


TOKEN_RE = re.compile(r"[A-Za-z_$][\w$]*|\d+")


def token_bag(text):
    bag = {}
    for tok in TOKEN_RE.findall(text):
        bag[tok] = bag.get(tok, 0) + 1
    return bag


def bag_diff(a_bag, b_bag):
    diffs = []
    keys = set(a_bag) | set(b_bag)
    for key in sorted(keys):
        ca = a_bag.get(key, 0)
        cb = b_bag.get(key, 0)
        if ca != cb:
            diffs.append("{0}:{1}->{2}".format(key, ca, cb))
    return "[" + ",".join(diffs[:40]) + "]" if diffs else "[]"


if __name__ == "__main__":
    main()
