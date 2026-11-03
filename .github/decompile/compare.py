#!/usr/bin/env python3
"""Compare decompiled v1 jar source (dec/radmin/client) with upstream meteor 26.1 tree.

Outputs under dec/_diff:
  report.txt  - CHANGED/NEW entries
  patches/    - unified diff per changed file
"""
import os
import re
import difflib

DEC = os.path.join("dec", "radmin", "client")
UP = os.path.join("w261", "src", "main", "java", "meteordevelopment", "meteorclient")
OUT = os.path.join("dec", "_diff")


def norm(path):
    try:
        with open(path, encoding="utf-8", errors="replace") as fh:
            text = fh.read()
    except OSError:
        return None
    text = re.sub(r"//.*", "", text)
    text = re.sub(r"/\*.*?\*/", "", text, flags=re.S)
    text = re.sub(r"\s+", "", text)
    return text


def main():
    os.makedirs(os.path.join(OUT, "patches"), exist_ok=True)
    report = []
    changed = new_count = 0
    for root, _dirs, files in os.walk(DEC):
        for fn in sorted(files):
            if not fn.endswith(".java"):
                continue
            dpath = os.path.join(root, fn)
            rel = os.path.relpath(dpath, DEC).replace(os.sep, "/")
            upath = os.path.join(UP, rel.replace("/", os.sep))
            if not os.path.isfile(upath):
                new_count += 1
                report.append("NEW " + rel)
                continue
            if norm(dpath) != norm(upath):
                changed += 1
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
                report.append("CHANGED " + rel + " -> patches/" + pname)
    report.insert(0, "CHANGED={0} NEW={1}".format(changed, new_count))
    with open(os.path.join(OUT, "report.txt"), "w", encoding="utf-8", newline="\n") as fh:
        fh.write("\n".join(report) + "\n")
    print("done changed={0} new={1}".format(changed, new_count))


if __name__ == "__main__":
    main()
